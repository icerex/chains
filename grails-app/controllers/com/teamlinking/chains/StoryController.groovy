package com.teamlinking.chains

import com.alibaba.fastjson.JSON
import com.teamlinking.chains.common.Base32Util
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.domain.NodeService
import com.teamlinking.chains.domain.StoryService
import com.teamlinking.chains.domain.UserService
import com.teamlinking.chains.wechat.AuthService
import org.apache.commons.lang.StringUtils

import javax.servlet.http.Cookie

class StoryController {

    AuthService authService
    StoryService storyService
    UserService userService
    NodeService nodeService

    def index() {
        String baseId = params."baseId" as String
        if (StringUtils.isEmpty(baseId)){
            redirect(url: "/error")
            return
        }
        String idStr = Base32Util.deCode32(baseId)
        long id = Long.parseLong(idStr)
        Story story = storyService.get(id)
        if (story == null){
            redirect(url: "/error")
            return
        }

        User user = userService.get(story.uid)

        String desc = "asc"
        def cookieMap = authService.readCookieMap(request)
        if (cookieMap.get(Constants.WECHAT_STORY_ID+id) == null){
            Cookie ckStoryId = new Cookie(Constants.WECHAT_STORY_ID+id, ""+id);
            ckStoryId.setMaxAge(86400000 * 7)
            ckStoryId.setPath("/")
            response.addCookie(ckStoryId)
        }else {
            desc = "desc"
        }

        render(view: "index", model: [
                story: story,
                user: user,
                desc: desc
        ])
    }

    def current(){
        //判断是否需要授权
        if (!authService.isNotAuth(request, response)) {
            //跳转到授权页面
            redirect(url: authService.getAuthBackUrl(grailsApplication.config,request))
        }else {
            String openId = request.getAttribute("openId")
            def story = storyService.getCurrentStory(openId)
            redirect(url: "/1/story/"+Base32Util.enCode32(story.id+""))
        }

    }

    def node(){
        long storyId = params.long("storyId",-1)
        if (storyId <= 0){
            redirect(url: "/error")
            return
        }
        int max = params.int('max', 20)
        int offset = params.int('offset', 0)
        int page = params.int('page', 1)
        if (page > 1){
            offset = (page-1) * 20
        }

        String desc = params."desc" as String
        if (desc == null){
            desc = "asc"
        }

        def vo = nodeService.list(storyId,max,offset,desc)

        def result = [:]
        result.status = 1
        result.data = vo
        result.hasNext = vo.count - offset - max > 0

        withFormat {
            json {
                render text: JSON.toJSONString(result), contentType: 'application/json;', encoding: "UTF-8"
            }
        }
    }
}
