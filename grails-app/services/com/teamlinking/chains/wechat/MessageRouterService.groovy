package com.teamlinking.chains.wechat

import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.wechat.handler.HelpEventService
import com.teamlinking.chains.wechat.handler.LocationEventService
import com.teamlinking.chains.wechat.handler.MenuCommandClickEventService
import com.teamlinking.chains.wechat.handler.MenuCurrentStoryClickEventService
import com.teamlinking.chains.wechat.handler.MessageAudioHandlerService
import com.teamlinking.chains.wechat.handler.MessageImageHandlerService
import com.teamlinking.chains.wechat.handler.MessageLocationHandlerService
import com.teamlinking.chains.wechat.handler.MessageTextHandlerService
import com.teamlinking.chains.wechat.handler.MessageVideoHandlerService
import com.teamlinking.chains.wechat.handler.SubscribeEventService
import com.teamlinking.chains.wechat.handler.UndoCommandEventService
import com.teamlinking.chains.wechat.interceptor.DataInterceptorService
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
    UndoCommandEventService undoCommandEventService
    LocationEventService locationEventService
    HelpEventService helpEventService

    MessageTextHandlerService messageTextHandlerService
    MessageImageHandlerService messageImageHandlerService
    MessageAudioHandlerService messageAudioHandlerService
    MessageVideoHandlerService messageVideoHandlerService
    MessageLocationHandlerService messageLocationHandlerService

    DataInterceptorService dataInterceptorService

    WxMpXmlOutMessage route(WxMpXmlMessage inMessage) {
        wxMpMessageRouter.route(inMessage)
    }

    public void afterPropertiesSet() throws Exception {
        wxMpMessageRouter = new WxMpMessageRouter(wxMpService)
        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_SUBSCRIBE).handler(subscribeEventService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_TEXT).content(Constants.WECHAT_MSGTYPE_TEXT_COMMAND_HELP).handler(helpEventService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.BUTTON_CLICK).eventKey(Constants.WechatMenu.undo.key).handler(undoCommandEventService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.BUTTON_CLICK).eventKey(Constants.WechatMenu.currentStory.key).handler(menuCurrentStoryClickEventService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.BUTTON_CLICK).handler(menuCommandClickEventService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().msgType(WxConsts.XML_MSG_EVENT).event(WxConsts.EVT_LOCATION).handler(locationEventService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_TEXT).content(Constants.WECHAT_MSGTYPE_TEXT_COMMAND_UNDO).handler(undoCommandEventService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_TEXT).handler(messageTextHandlerService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_IMAGE).handler(messageImageHandlerService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_VOICE).handler(messageAudioHandlerService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_VIDEO).handler(messageVideoHandlerService).interceptor(dataInterceptorService).end()

        wxMpMessageRouter = wxMpMessageRouter.rule().async(false).msgType(WxConsts.XML_MSG_LOCATION).handler(messageLocationHandlerService).interceptor(dataInterceptorService).end()

    }
}
