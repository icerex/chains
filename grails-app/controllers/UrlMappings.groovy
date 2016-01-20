class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/api/1.0/wechat/callback"(controller: 'wechat', action: 'callback')
        "/api/1.0/wechat/signature"(controller: 'wechat', action: 'signature')
        "/api/1.0/wechat/createMenu"(controller: 'wechat', action: 'createMenu')
        "/api/1.0/wechat/authback"(controller: 'wechat', action: 'authback')

        "/api/1.0/qiniu/callback"(controller: 'qiniu', action: 'callback')

        "/1/story/$baseId"(controller: "story", action: 'index')
        "/1/story/current"(controller: "story", action: 'current')
        "/1/story/node.json"(controller: 'story', action: 'node')

        "/1/user/$baseId"(controller: "user", action: 'index')
        "/1/user/current"(controller: "user", action: 'current')
        "/1/user/story.json"(controller: "user", action: 'story')

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
