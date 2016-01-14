class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }
        "/api/1.0/wechat/callback"(controller: 'wechat', action: 'callback')
        "/api/1.0/qiniu/callback"(controller: 'qiniu', action: 'callback')

        "/"(view:"/index")
        "500"(view:'/error')
        "404"(view:'/notFound')
    }
}
