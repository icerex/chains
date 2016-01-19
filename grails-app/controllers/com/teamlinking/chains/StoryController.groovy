package com.teamlinking.chains

import com.teamlinking.chains.common.Base32Util
import com.teamlinking.chains.domain.StoryService
import com.teamlinking.chains.wechat.AuthService

class StoryController {

    AuthService authService
    StoryService storyService

    def index() {


    }

    def current(){
        //判断是否需要授权
        if (!authService.isNotAuth(request, response)) {
            //跳转到授权页面
            redirect(url: authService.getCallBackUrl(grailsApplication.config,request))
        }else {
            String openId = request.getAttribute("openId")
            def story = storyService.getCurrentStory(openId)
            redirect(url: "/1/story/"+Base32Util.enCode32(story.id+""))
        }

    }
}
