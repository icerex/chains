package com.teamlinking.chains.wechat.eventbus

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.google.common.eventbus.Subscribe
import com.teamlinking.chains.Node
import com.teamlinking.chains.Story
import com.teamlinking.chains.UploadRecord
import com.teamlinking.chains.common.CommonUtil
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.eventbus.UploadEvent
import com.teamlinking.chains.storage.AvinfoResult
import com.teamlinking.chains.storage.ImageResult
import com.teamlinking.chains.storage.QiniuUpload
import com.teamlinking.chains.storage.UploadResult
import com.teamlinking.chains.wechat.MediaDownloadExecutor
import me.chanjar.weixin.mp.api.WxMpService

class UploadListenerService {

    QiniuUpload qiniuUpload
    WxMpService wxMpService

    @Subscribe
    public void lister(final UploadEvent event) {
        byte[] bytes  = download(event.mediaId)
        String key = CommonUtil.uploadPrimaryKey(bytes)

        switch (event.fileType){
            case Constants.FileType.pic:
                UploadRecord record = getRecord(key)
                if (record == null) {
                    ImageResult result = qiniuUpload.uploadImage(bytes, key)
                    record = saveRecord(result, event)
                }
                if (record) {
                    if (event.ownerType == Constants.OwnerType.story) {
                        Story story = Story.get(event.ownerId)
                        if (story) {
                            story.pic = record.url
                            story.lastUpdated = new Date()
                            story.save(flush: true, failOnError: true)
                        }
                    } else {
                        Node node = Node.get(event.ownerId)
                        if (node) {
                            node.picUrl = record.url
                            node.lastUpdated = new Date()
                            node.save(flush: true, failOnError: true)
                        }
                    }
                }
                break
            case Constants.FileType.audio:
                UploadRecord record = getRecord(key)
                double duration = 0
                if (record == null) {
                    AvinfoResult result = qiniuUpload.uploadAudio(bytes,key)
                    record = saveRecord(result,event)
                    duration = result.duration
                }else {
                    def json = JSON.parseObject(record.params)
                    if (json) {
                        duration = json.getDoubleValue("duration")
                    }
                }
                if (record){
                    Node node = Node.get(event.ownerId)
                    if (node && node.audioId){
                        if (node.audioId.equals(event.mediaId)) {
                            node.audioUrl = record.url
                            node.audioLoadState = Constants.AvLoadState.transcoding.value
                            node.audioDuration = duration
                            node.lastUpdated = new Date()
                            node.save(flush: true, failOnError: true)
                        }
                    }
                }

                break
            case Constants.FileType.video:
                UploadRecord record = getRecord(key)
                double duration = 0
                if (record == null) {
                    AvinfoResult result = qiniuUpload.uploadvedio(bytes,key)
                    record = saveRecord(result,event)
                    duration = result.duration
                }else {
                    def json = JSON.parseObject(record.params)
                    if (json) {
                        duration = json.getDoubleValue("duration")
                    }
                }
                if (record){
                    Node node = Node.get(event.ownerId)
                    if (node && node.videoId){
                        if (node.videoId.equals(event.mediaId)) {
                            node.videoUrl = record.url
                            node.videoLoadState = Constants.AvLoadState.transcoding.value
                            node.videoDuration = duration
                            node.lastUpdated = new Date()
                            node.save(flush: true, failOnError: true)
                        }
                    }
                }
                break
        }


    }

    byte[] download(String mediaId){
        String url = "http://file.api.weixin.qq.com/cgi-bin/media/get";
        return wxMpService.execute(new MediaDownloadExecutor(),url,"media_id=" + mediaId)
    }

    UploadRecord saveRecord(UploadResult result, UploadEvent event){
        if (result) {
            return new UploadRecord(
                    dateCreated: new Date(),
                    lastUpdated: new Date(),
                    key: result.key,
                    ownerId: event.ownerId,
                    ownerType: event.ownerType.value,
                    fileType: event.fileType.value,
                    url: Constants.QINIU_DOMAIN + result.key,
                    params: result.toString()
            ).save()
        }
        return null
    }

    UploadRecord getRecord(String key){
        return UploadRecord.findByKey(key)
    }
}
