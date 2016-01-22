package com.teamlinking.chains.wechat.eventbus

import com.google.common.eventbus.AsyncEventBus
import com.google.common.eventbus.Subscribe
import com.teamlinking.chains.eventbus.UploadEvent
import org.springframework.beans.factory.InitializingBean

import java.util.concurrent.Executors

/**
 * 异步上传
 */
class UploadEventBusService implements InitializingBean {

    AsyncEventBus bus

    UploadListenerService uploadListenerService

    def post(UploadEvent event){
        bus.post(event)
    }

    @Override
    void afterPropertiesSet() throws Exception {
        bus = new AsyncEventBus(Executors.newFixedThreadPool(10))
        //注册上传事件
        bus.register(new Listener(uploadListenerService))
    }

    class Listener{
        private UploadListenerService listener

        Listener(UploadListenerService listener){
            this.listener = listener
        }

        @Subscribe
        public void execute(UploadEvent event) {
            listener.lister(event)
        }
    }
}
