package com.teamlinking.chains.wechat

import com.teamlinking.chains.common.Constans
import com.teamlinking.chains.wechat.handler.MenuCommandClickEventService
import com.teamlinking.chains.wechat.handler.MenuCurrentStoryClickEventService
import com.teamlinking.chains.wechat.handler.SubscribeEventService
import com.teamlinking.chains.wechat.interceptor.MenuCommandInterceptorService
import me.chanjar.weixin.common.api.WxConsts
import me.chanjar.weixin.mp.api.WxMpMessageRouter
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage
import org.springframework.beans.factory.InitializingBean

/**
 * 消息路由
 */
class MessageRouterService implements InitializingBean {

    WxMpService wxMpService
    WxMpMessageRouter wxMpMessageRouter
    SubscribeEventService subscribeEventService
    MenuCurrentStoryClickEventService menuCurrentStoryClickEventService
    MenuCommandClickEventService menuCommandClickEventService

    MenuCommandInterceptorService menuCommandInterceptorService

    WxMpXmlOutMessage route(WxMpXmlMessage inMessage) {
        wxMpMessageRouter.route(inMessage)
    }

    public void afterPropertiesSet() throws Exception {
        wxMpMessageRouter = new WxMpMessageRouter(wxMpService)
        wxMpMessageRouter
                .rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SUBSCRIBE).handler(subscribeEventService).end()
                .rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_CLICK).eventKey(Constans.WechatMenu.currentStory.key).handler(menuCurrentStoryClickEventService).end()
                .rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.BUTTON_CLICK).handler(menuCommandClickEventService).interceptor(menuCommandInterceptorService).end()

    }
}
