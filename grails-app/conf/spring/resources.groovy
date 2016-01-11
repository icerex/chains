import grails.util.Environment
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage
import me.chanjar.weixin.mp.api.WxMpServiceImpl

// Place your Spring DSL code here
beans = {
    switch (Environment.current) {
        case Environment.PRODUCTION:
            wxMpConfigStorage(WxMpInMemoryConfigStorage){
                appId = "wxef4f18e4e555cb98"
                secret = "a5a0562f84205e7b5aef8ab875d2810a"
                token = "098f6bcd4621d373cade4e832627b4f6"
                aesKey = "IlEG9ZQXgDms3RPei76Azfh9pAEX3Hn8xwb1JDkbiY1"
            }

            break
        case Environment.DEVELOPMENT:
        case Environment.TEST:
            wxMpConfigStorage(WxMpInMemoryConfigStorage){
                appId = "wxef4f18e4e555cb98"
                secret = "a5a0562f84205e7b5aef8ab875d2810a"
                token = "098f6bcd4621d373cade4e832627b4f6"
                aesKey = "IlEG9ZQXgDms3RPei76Azfh9pAEX3Hn8xwb1JDkbiY1"
            }

            break
    }

    wxMpService(WxMpServiceImpl){
        wxMpConfigStorage = wxMpConfigStorage
    }

}
