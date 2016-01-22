package com.teamlinking.chains.wechat.eventbus

import com.google.common.eventbus.AsyncEventBus
import com.teamlinking.chains.eventbus.UploadEvent
import org.springframework.beans.factory.InitializingBean

/**
 * 异步上传
 */
class UploadEventBusService implements InitializingBean {

    final AsyncEventBus bus = new AsyncEventBus()

    UploadListenerService uploadListenerService

    def post(UploadEvent event){
        bus.post(event)
    }

    @Override
    void afterPropertiesSet() throws Exception {
        //注册上传事件
        bus.register(uploadListenerService)
    }
}
