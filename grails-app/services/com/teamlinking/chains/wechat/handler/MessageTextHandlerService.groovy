package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Node
import com.teamlinking.chains.Story
import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.CommonUtil
import com.teamlinking.chains.common.Constans
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

    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        Story currentStory = context.get("currentStory") as Story
        UserState userState = context.get("userState") as UserState
        String content = null
        if (userState.command) {
            //执行命令
            switch (userState.command){
                case Constans.WechatCommand.story_add.key:
                    if (CommonUtil.length(wxMessage.content) > 10){
                        content = Constans.WECHAT_MSG_ADD_STORY_TEXT_LENGTH
                    }else {
                        Story story = new Story(
                                dateCreated: new Date(),
                                lastUpdated: new Date(),
                                uid: userState.uid,
                                parentId: currentStory.parentId,
                                title: wxMessage.content
                        )
                        story = story.save()
                        userState.currentStoryId = story.id
                        userState.command = Constans.WechatCommand.story_image_add.key
                        userState.lastUpdated = new Date()
                        userState.save(flush: true, failOnError: true)
                    }
                    break
                case Constans.WechatCommand.story_sub_add.key:
                    if (CommonUtil.length(wxMessage.content) > 10){
                        content = Constans.WECHAT_MSG_ADD_STORY_TEXT_LENGTH
                    }else {
                        Story story = new Story(
                                dateCreated: new Date(),
                                lastUpdated: new Date(),
                                uid: userState.uid,
                                parentId: currentStory.id,
                                title: wxMessage.content
                        )
                        story = story.save()
                        userState.currentStoryId = story.id
                        userState.command = Constans.WechatCommand.story_image_add.key
                        userState.lastUpdated = new Date()
                        userState.save(flush: true, failOnError: true)
                    }
                    break
                case Constans.WechatCommand.story_upate.key:
                    if (CommonUtil.length(wxMessage.content) > 10){
                        content = Constans.WECHAT_MSG_ADD_STORY_TEXT_LENGTH
                    }else {
                        currentStory.title = wxMessage.content
                        currentStory.lastUpdated = new Date()
                        currentStory.save()
                        userState.command = null
                        userState.lastUpdated = new Date()
                        userState.save(flush: true, failOnError: true)
                        content = String.format(Constans.WECHAT_MSG_ADD_STORY_IMAGE, currentStory.title)
                    }
                    break
            }
        }else{
            //记录节点
            Node node = nodeService.saveByContent(userState,wxMessage.content)
            wechatMessageService.insert(userState.uid,node.id,wxMessage)
            content = Constans.WECHAT_MSG_NODE_IMAGE_SUCCESS
        }

        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

}
