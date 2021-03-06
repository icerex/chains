package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Node
import com.teamlinking.chains.Story
import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.CommonUtil
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.domain.NodeService
import com.teamlinking.chains.domain.WechatMessageService
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage

/**
 * 文本消息处理
 */
class MessageTextHandlerService implements WxMpMessageHandler{

    NodeService nodeService
    WechatMessageService wechatMessageService

    final int TITLE_LENGTH = 20

    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        Story currentStory = context.get("currentStory") as Story
        UserState userState = context.get("userState") as UserState
        String content = null
        if (userState.command) {
            //执行命令
            switch (userState.command){
                case Constants.WechatCommand.story_add.key:
                    if (CommonUtil.length(wxMessage.content) > TITLE_LENGTH){
                        content = Constants.WECHAT_MSG_ADD_STORY_TEXT_LENGTH
                    }else {
                        Story story = new Story(
                                dateCreated: new Date(),
                                lastUpdated: new Date(),
                                uid: userState.uid,
                                title: wxMessage.content,
                                pic: Constants.DEFAULT_STORY_BACK_GROUND
                        )
                        story = story.save()
                        userState.currentStoryId = story.id
                        userState.command = Constants.WechatCommand.story_image_add.key
                        userState.lastUpdated = new Date()
                        userState.save(flush: true, failOnError: true)
                        content = String.format(Constants.WECHAT_MSG_ADD_STORY_IMAGE, story.title)
                    }
                    break
                case Constants.WechatCommand.story_sub_add.key:
                    if (CommonUtil.length(wxMessage.content) > TITLE_LENGTH){
                        content = Constants.WECHAT_MSG_ADD_STORY_TEXT_LENGTH
                    }else {
                        Story story = new Story(
                                dateCreated: new Date(),
                                lastUpdated: new Date(),
                                uid: userState.uid,
                                parentId: currentStory.id,
                                title: wxMessage.content,
                                pic: Constants.DEFAULT_STORY_BACK_GROUND
                        )
                        story = story.save()
                        userState.currentStoryId = story.id
                        userState.command = Constants.WechatCommand.story_image_add.key
                        userState.lastUpdated = new Date()
                        userState.save(flush: true, failOnError: true)
                        content = String.format(Constants.WECHAT_MSG_ADD_STORY_IMAGE, story.title)
                    }
                    break
                case Constants.WechatCommand.story_upate.key:
                    if (CommonUtil.length(wxMessage.content) > TITLE_LENGTH){
                        content = Constants.WECHAT_MSG_ADD_STORY_TEXT_LENGTH
                    }else {
                        currentStory.title = wxMessage.content
                        currentStory.lastUpdated = new Date()
                        currentStory.save()
                        userState.command = null
                        userState.lastUpdated = new Date()
                        userState.save(flush: true, failOnError: true)
                        content = String.format(Constants.WECHAT_MSG_UPDATE_STORY_IMAGE_AFTER, currentStory.title)
                    }
                    break
                default:
                    content = String.format(Constants.WECHAT_MSG_NODE_FAILE,Constants.WechatCommand.pase(userState.command).value)
            }
        }else{
            if (CommonUtil.matcherGroup(wxMessage.content,Constants.DATE_SET_EL)){
                Date date = CommonUtil.matcherDate(wxMessage.content)
                if (date){
                    //设置节点时间
                    Node node = nodeService.setNodeTime(userState,date)
                    if (node) {
                        content = Constants.WECHAT_MSG_NODE_DATE_SUCCESS
                    }else {
                        content = Constants.WECHAT_MSG_NODE_NO_DATA_FAILE
                    }
                }else {
                    content = Constants.WECHAT_MSG_NODE_DATE_FAILE
                }

            }else {
                //记录节点
                Node node = nodeService.saveByContent(userState, wxMessage.content)
                wechatMessageService.insert(userState.uid, node.id, wxMessage)
                content = Constants.WECHAT_MSG_NODE_TEXT_SUCCESS
            }
        }

        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

}
