package com.teamlinking.chains

import com.alibaba.fastjson.JSON
import com.teamlinking.chains.storage.AvItem
import com.teamlinking.chains.storage.AvThBack

class QiniuController {

    def callback() {
        def jsonReq = request.JSON

        AvThBack avBack = new AvThBack()
        avBack.code = jsonReq.code //
        avBack.id = jsonReq.id
        avBack.inputBucket = jsonReq.inputBucket
        avBack.pipeline = jsonReq.pipeline
        avBack.inputKey = jsonReq.inputKey

        log.info("---> qiniu back --- id:" + avBack.id + "  |  to see op result :" + "http://api.qiniu.com/status/get/prefop?id=" + avBack.id)

        try {
            jsonReq.items.each { it ->
                AvItem item = new AvItem()
                item.cmd = it.cmd
                item.desc = it.desc
                item.hash = it.hash
                item.key = it.key
                item.returnOld = it.returnOld
                avBack.getItems().add(item)
                log.info("save qiniu call back info success......persistant id ->" + avBack.id)
            }
        } catch (Exception e) {
            log.error("---> save qiniu call back request:"+ jsonReq)
            log.error("---> save qiniu call back info failed.......excp:" + e.getMessage(), e)
        }
        def result = [:]
        /**
         * todo 处理回调
         */
        result.status = 1

        withFormat {
            json {
                render text: JSON.toJSONString(result), contentType: 'application/json;', encoding: "UTF-8"
            }
        }
        return
    }
}
