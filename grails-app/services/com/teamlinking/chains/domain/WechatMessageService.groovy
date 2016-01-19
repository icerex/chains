package com.teamlinking.chains.domain

import com.teamlinking.chains.WechatMessage
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.util.xml.XStreamTransformer

class WechatMessageService {

    WechatMessage getLastMessage(long uid) {
        WechatMessage.findAllByUid(uid,[max: 1, sort: "dateCreated", order: "desc"]).each {
            return it
        }
        return null
    }

    def insert(long uid,long nodeId,WxMpXmlMessage wxMpXmlMessage){
        WechatMessage message = new WechatMessage(
                dateCreated: new Date(),
                lastUpdated: new Date(),
                uid: uid,
                msgId: wxMpXmlMessage.msgId,
                msgType: wxMpXmlMessage.msgType,
                openId: wxMpXmlMessage.fromUserName,
                body: XStreamTransformer.toXml(WxMpXmlMessage.class,wxMpXmlMessage),
                nodeId: nodeId
        )
        message.save(flush: true, failOnError: true)
    }
}
