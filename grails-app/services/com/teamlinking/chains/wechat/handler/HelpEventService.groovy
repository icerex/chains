package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.common.Constants
import grails.util.Environment
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage

/**
 * 帮助命令事件
 */
class HelpEventService implements WxMpMessageHandler{


    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        switch (Environment.current) {
            case Environment.PRODUCTION:
                item.setDescription(Constants.WECHAT_HELP_PRODUCT_description);
                item.setPicUrl(Constants.WECHAT_HELP_PRODUCT_picUrl);
                item.setTitle(Constants.WECHA_HELPT_PRODUCT_title);
                item.setUrl(Constants.WECHA_HELPT_PRODUCT_url);
                break
            case Environment.DEVELOPMENT:
            case Environment.TEST:
                item.setDescription(Constants.WECHAT_HELP_TEST_description);
                item.setPicUrl(Constants.WECHAT_HELP_TEST_picUrl);
                item.setTitle(Constants.WECHA_HELPT_TEST_title);
                item.setUrl(Constants.WECHA_HELPT_TEST_url);
                break
        }

        return WxMpXmlOutMessage.NEWS().fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).addArticle(item).build();
    }

}
