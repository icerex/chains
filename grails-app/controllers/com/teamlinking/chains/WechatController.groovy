package com.teamlinking.chains

import com.alibaba.fastjson.JSON
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.domain.StoryService
import com.teamlinking.chains.domain.UserService
import com.teamlinking.chains.wechat.MessageRouterService
import me.chanjar.weixin.common.bean.WxMenu
import me.chanjar.weixin.mp.api.WxMpConfigStorage
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken
import me.chanjar.weixin.mp.bean.result.WxMpUser
import org.apache.commons.lang.StringUtils

import javax.servlet.http.Cookie

class WechatController {

    WxMpService wxMpService
    WxMpConfigStorage wxMpConfigStorage
    MessageRouterService messageRouterService
    UserService userService
    StoryService storyService

    def callback(){
        String signature = params."signature" as String
        String nonce = params."nonce" as String
        String timestamp = params."timestamp" as String

        def result = null

        if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(nonce) || StringUtils.isEmpty(timestamp) ){
            result = "Parameter Error"
        }else if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            String echostr = params."echostr" as String
            if (StringUtils.isEmpty(echostr) ){
                String encryptType = params."encrypt_type" as String
                if (StringUtils.isEmpty(encryptType)){
                    encryptType = "raw"
                }
                if ("raw".equals(encryptType)){
                    // 明文传输的消息
                    WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream())
                    WxMpXmlOutMessage outMessage = messageRouterService.route(inMessage)
                    result = outMessage.toXml()
                }else if ("aes".equals(encryptType)){
                    // 是aes加密的消息
                    String msgSignature = params."msg_signature" as String
                    if (StringUtils.isEmpty(msgSignature)){
                        result = "Parameter Error"
                    }else {
                        WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature)
                        WxMpXmlOutMessage outMessage = messageRouterService.route(inMessage)
                        result = outMessage.toXml()
                    }
                }else {
                    result = "Non identifiable encryption type"
                }
            }else {
                result = echostr
            }
        }else {
            result = "Illegal Request"
        }

        withFormat {
            json {
                render text: result, contentType: 'text/xml;', encoding: "UTF-8"
            }
        }
    }

    def signature() {
        String url = params."url" as String

        def sign = wxMpService.createJsapiSignature(url)

        def result = [:]
        result.put("appId", sign.appid)
        result.put("timestamp", sign.timestamp)
        result.put("nonceStr", sign.noncestr)
        result.put("signature", sign.signature)

        withFormat {
            json {
                render text: JSON.toJSONString(result), contentType: 'application/json;', encoding: "UTF-8"
            }
        }
    }

    def authback(){
        String code = params."code" as String
        String state = params."state" as String
        if (code) {
            //获取access token
            WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code)

            if (wxMpOAuth2AccessToken) {
                //获取用户信息
                WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null)
                if (wxMpUser) {

                    User user = userService.initUser(wxMpUser)
                    Story story = storyService.initMasterStory(user.id)
                    userService.initState(user.id,story.id)

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

        if (state) {
            redirect(url: state)
        }else {
            redirect(url: "/error")
        }
    }

    def createMenu(){
        String protocol = grailsApplication.config.getProperty("protocol")
        String domain = grailsApplication.config.getProperty("domain")
        wxMpService.menuDelete()
        String json = '{' +
                '  "menu": {' +
                '    "button": [' +
                '      {' +
                '        "type": "click",' +
                '        "name": "新增",' +
                '        "sub_button": [' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constants.WechatMenu.addStory.value+'",' +
                '            "key": "'+ Constants.WechatMenu.addStory.key + '"' +
                '          },' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constants.WechatMenu.addSubStory.value+'",' +
                '            "key": "'+ Constants.WechatMenu.addSubStory.key + '"' +
                '          },' +
                '        ]' +
                '      },' +
                '      {' +
                '        "type": "click",' +
                '        "name": "编辑",' +
                '        "sub_button": [' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constants.WechatMenu.updateStory.value+'",' +
                '            "key": "'+ Constants.WechatMenu.updateStory.key + '"' +
                '          },' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constants.WechatMenu.nextStory.value+'",' +
                '            "key": "'+ Constants.WechatMenu.nextStory.key + '"' +
                '          },' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constants.WechatMenu.backParent.value+'",' +
                '            "key": "'+ Constants.WechatMenu.backParent.key + '"' +
                '          },' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constants.WechatMenu.undo.value+'",' +
                '            "key": "'+ Constants.WechatMenu.undo.key + '"' +
                '          }' +
                '        ]' +
                '      },' +
                '      {' +
                '        "name": "查看",' +
                '        "sub_button": [' +
                '          {' +
                '            "type": "click",' +
                '            "name": "'+Constants.WechatMenu.currentStory.value+'",' +
                '            "key": "'+ Constants.WechatMenu.currentStory.key + '"' +
                '          },' +
                '          {' +
                '            "type": "view",' +
                '            "name": "当前主题",' +
                '            "url": "'+ protocol + domain +'/1/story/current"' +
                '          },' +
                '          {' +
                '            "type": "view",' +
                '            "name": "个人中心",' +
                '            "url": "'+ protocol + domain +'/1/user/current"' +
                '          }' +
                '        ]' +
                '      }' +
                '    ]' +
                '  }' +
                '}'
        WxMenu wxMenu = WxMenu.fromJson(json)
        wxMpService.menuCreate(wxMenu)

        withFormat {
            json {
                render text: "Wechat menu create success!", contentType: 'application/json;', encoding: "UTF-8"
            }
        }
    }
}
