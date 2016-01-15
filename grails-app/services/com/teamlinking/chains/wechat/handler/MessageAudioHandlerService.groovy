package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Story
import com.teamlinking.chains.UserState
import grails.transaction.Transactional
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage

/**
 * 音频消息处理
 */
class MessageAudioHandlerService implements WxMpMessageHandler{

    @Override
    @Transactional
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        Story currentStory = context.get("currentStory") as Story
        UserState userState = context.get("userState") as UserState
        String content = null
        if (userState.command) {
            //执行命令

        }else{
            //记录节点

        }

        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

}
