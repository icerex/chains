package com.teamlinking.chains.wechat

import com.teamlinking.chains.common.Constants
import grails.config.Config
import me.chanjar.weixin.common.api.WxConsts
import me.chanjar.weixin.common.util.http.URIUtil
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken

import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthService {

    WxMpService wxMpService

    String getCallBackUrl(Config config, HttpServletRequest request){
        String protocol = config.getProperty("protocol")
        String domain = config.getProperty("domain")
        String state = URIUtil.encodeURIComponent(protocol + domain + request.getRequestURI())
        String redirectUrl = protocol + domain + "/1/wechat/callback"
        return wxMpService.oauth2buildAuthorizationUrl(redirectUrl, WxConsts.OAUTH2_SCOPE_USER_INFO, state)
    }

    /**
     * 是否需要授权
     * @param request
     * @param response
     * @return
     */
    boolean isNotAuth(HttpServletRequest request, HttpServletResponse response) {
        boolean isNotAuth = true
        String userAgent = request.getHeader("User-Agent")
        if (userAgent.toLowerCase().contains('micromessenger')) {
            isNotAuth = false
            def cookieMap = readCookieMap(request)
            String accessToken = cookieMap.get(Constants.WECHAT_ACCESSTOKEN)
            String openId = cookieMap.get(Constants.WECHAT_OPEN_ID)
            String refreshToken = cookieMap.get(Constants.WECHAT_REFRESHTOKEN)
            String unionId = cookieMap.get(Constants.WECHAT_UNION_ID)

            if (accessToken && openId && unionId) {
                WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken()
                wxMpOAuth2AccessToken.openId = openId
                wxMpOAuth2AccessToken.accessToken = accessToken

                boolean valid = false
                try {
                    //验证access token
                    if (wxMpService.oauth2validateAccessToken(wxMpOAuth2AccessToken)){
                        isNotAuth = true
                        request.setAttribute("openId",openId)
                    }
                } catch (Exception e) {
                }

            } else if (refreshToken && unionId) {
                //刷新token
                WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null

                try {
                    wxMpOAuth2AccessToken = wxMpService.oauth2refreshAccessToken(refreshToken)
                } catch (Exception e) {
                }

                if (wxMpOAuth2AccessToken) {
                    isNotAuth = true
                    request.setAttribute("openId",wxMpOAuth2AccessToken.openId)

                    //设置cookie
                    Cookie ckOpenId = new Cookie(Constants.WECHAT_OPEN_ID, wxMpOAuth2AccessToken.openId)
                    ckOpenId.setMaxAge(7200)
                    ckOpenId.setPath("/")
                    response.addCookie(ckOpenId)

                    Cookie ckAccessToken = new Cookie(Constants.WECHAT_ACCESSTOKEN, wxMpOAuth2AccessToken.accessToken);
                    ckAccessToken.setMaxAge(7200)
                    ckAccessToken.setPath("/")
                    response.addCookie(ckAccessToken)

                    Cookie ckRefreshToken = new Cookie(Constants.WECHAT_REFRESHTOKEN, wxMpOAuth2AccessToken.refreshToken);
                    ckRefreshToken.setMaxAge(86400000 * 7)
                    ckRefreshToken.setPath("/")
                    response.addCookie(ckRefreshToken)

                    Cookie ckUnionId = new Cookie(Constants.WECHAT_UNION_ID, wxMpOAuth2AccessToken.unionId);
                    ckUnionId.setMaxAge(86400000 * 7)
                    ckUnionId.setPath("/")
                    response.addCookie(ckUnionId)
                }
            }

        }
        return isNotAuth
    }

    /**
      * 将cookie封装到Map里面
      * @param request
      * @return
      */
    Map<String, String> readCookieMap(HttpServletRequest request) {
        Map<String, String> cookieMap = new HashMap<String, String>()
        Cookie[] cookies = request.getCookies()
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.name, cookie.value)
            }
        }
        return cookieMap
    }
}
