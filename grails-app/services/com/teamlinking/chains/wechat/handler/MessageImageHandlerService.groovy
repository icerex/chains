package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Story
import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.domain.NodeService
import com.teamlinking.chains.domain.WechatMessageService
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage
import com.teamlinking.chains.Node

/**
 * 图片消息处理
 */
class MessageImageHandlerService implements WxMpMessageHandler{

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
                case Constants.WechatCommand.story_image_add.key:
                    currentStory.pic = wxMessage.picUrl
                    //todo 要上传
                    currentStory.lastUpdated = new Date()
                    currentStory.save()
                    cleanState(userState)
                    content = String.format(Constants.WECHAT_MSG_ADD_STORY_AFTER, currentStory.title)
                    break
                case Constants.WechatCommand.story_upate.key:
                    currentStory.pic = wxMessage.picUrl
                    //todo 要上传
                    currentStory.lastUpdated = new Date()
                    currentStory.save()
                    cleanState(userState)
                    content = String.format(Constants.WECHAT_MSG_UPDATE_STORY_IMAGE_AFTER, currentStory.title)
                    break
            }
        }else{
            //记录节点
            Node node = nodeService.saveByImage(userState,wxMessage.picUrl)
            wechatMessageService.insert(userState.uid,node.id,wxMessage)
            content = Constants.WECHAT_MSG_NODE_IMAGE_SUCCESS
            //todo 启动上传任务
        }

        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

    void cleanState(UserState userState){
        userState.command = null
        userState.lastUpdated = new Date()
        userState.save(flush: true, failOnError: true)
    }
}
