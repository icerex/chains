package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Story
import com.teamlinking.chains.User
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.domain.StoryService
import com.teamlinking.chains.domain.UserService
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage
import me.chanjar.weixin.mp.bean.result.WxMpUser

/**
 * 订阅公众号事件
 */
class SubscribeEventService implements WxMpMessageHandler{

    UserService userService
    StoryService storyService

    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        WxMpUser wxMpUser = wxMpService.userInfo(wxMessage.fromUserName,null)
        User user = userService.initUser(wxMpUser)
        Story story = storyService.initMasterStory(user.id)
        userService.initState(user.id,story.id)
        return WxMpXmlOutMessage.TEXT().content(Constants.WECHAT_MSG_SUBSCRIBE).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }
}
