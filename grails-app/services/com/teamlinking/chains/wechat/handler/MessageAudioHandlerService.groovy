package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Node
import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.domain.NodeService
import com.teamlinking.chains.domain.WechatMessageService
import com.teamlinking.chains.eventbus.UploadEvent
import com.teamlinking.chains.wechat.eventbus.UploadEventBusService
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
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

        return WxMpXmlOutMessage.TEXT().content( Constants.WECHAT_MSG_AUDIO_RETURN).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

}
