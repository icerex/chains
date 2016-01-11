package com.teamlinking.chains

import com.alibaba.fastjson.JSON
import me.chanjar.weixin.mp.api.WxMpService
import org.apache.commons.lang.StringUtils

class WechatController {

    WxMpService wxMpService

    def index() { }

    def checkSignature(){
        String signature = params."signature" as String
        String nonce = params."nonce" as String
        String timestamp = params."timestamp" as String

        def result = null

        if (StringUtils.isEmpty(signature) || StringUtils.isEmpty(nonce) || StringUtils.isEmpty(timestamp) ){
            result = "Parameter Error"
        }else if (wxMpService.checkSignature(timestamp, nonce, signature)) {
            result = params."echostr" as String
        }else {
            result = "Illegal Request"
        }

        withFormat {
            json {
                render text: result, contentType: 'application/json;', encoding: "UTF-8"
            }
        }
    }
}
