package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Node
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

/**
 * 地理位置消息处理
 */
class MessageLocationHandlerService implements WxMpMessageHandler{

    NodeService nodeService
    WechatMessageService wechatMessageService

    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        UserState userState = context.get("userState") as UserState
        String content = null
        if (userState.command){
            //如果在执行命令,不能接受地理位置
            content = Constants.WECHAT_MSG_NODE_FAILE
        }else{
            //记录节点
            Node node = nodeService.saveByLocation(userState,wxMessage.locationX,wxMessage.locationY,wxMessage.label)
            wechatMessageService.insert(userState.uid,node.id,wxMessage)
            content = Constants.WECHAT_MSG_NODE_LOCATION_SUCCESS
        }

        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }
}
