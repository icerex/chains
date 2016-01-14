package com.teamlinking.chains.wechat.interceptor

import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageInterceptor
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage

/**
 * 公众号命令类型菜单点击事件执行校验
 */
class MenuCommandInterceptorService implements WxMpMessageInterceptor{

    @Override
    boolean intercept(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        return false
    }
}
