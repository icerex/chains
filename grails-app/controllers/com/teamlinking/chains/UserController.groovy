package com.teamlinking.chains

import com.teamlinking.chains.common.Base32Util
import com.teamlinking.chains.domain.UserService
import com.teamlinking.chains.wechat.AuthService

class UserController {

    AuthService authService
    UserService userService

    def index() {

    }

    def current(){
        //判断是否需要授权
        if (!authService.isNotAuth(request, response)) {
            //跳转到授权页面
            redirect(url: authService.getAuthBackUrl(grailsApplication.config,request))
        }else {
            String openId = request.getAttribute("openId")
            def user = userService.get(openId)
            redirect(url: "/1/user/"+Base32Util.enCode32(user.id+""))
        }

    }
}
