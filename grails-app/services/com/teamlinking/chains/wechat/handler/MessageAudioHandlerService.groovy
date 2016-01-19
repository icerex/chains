package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Node
import com.teamlinking.chains.UserState
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
 * 音频消息处理
 */
class MessageAudioHandlerService implements WxMpMessageHandler{

    NodeService nodeService
    WechatMessageService wechatMessageService

    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        UserState userState = context.get("userState") as UserState
        //记录节点
        Node node = nodeService.saveByAudio(userState,wxMessage.mediaId)
        wechatMessageService.insert(userState.uid,node.id,wxMessage)
        String content = Constans.WECHAT_MSG_NODE_AUDIO_SUCCESS
        //todo 启动上传任务

        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

}
