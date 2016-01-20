package com.teamlinking.chains

import com.alibaba.fastjson.JSON
import com.teamlinking.chains.common.Base32Util
import com.teamlinking.chains.domain.StoryService
import com.teamlinking.chains.domain.UserService
import com.teamlinking.chains.wechat.AuthService
import org.apache.commons.lang.StringUtils

class UserController {

    AuthService authService
    UserService userService
    StoryService storyService

    def index() {
        String baseId = params."baseId" as String
        if (StringUtils.isEmpty(baseId)){
            redirect(url: "/error")
            return
        }
        String idStr = Base32Util.deCode32(baseId)
        long id = Long.parseLong(idStr)
        def user = userService.get(id)

        render(view: "index", model: [
                user: user
        ])
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

    def story(){
        long id = params.long("id",-1)
        if (id <= 0){
            redirect(url: "/error")
            return
        }

        def result = [:]
        result.status = 1

        withFormat {
            json {
                render text: JSON.toJSONString(result), contentType: 'application/json;', encoding: "UTF-8"
            }
        }

    }
}
