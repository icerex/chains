package com.teamlinking.chains.domain

import com.teamlinking.chains.Node
import com.teamlinking.chains.Story
import com.teamlinking.chains.UploadRecord
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.storage.AvThBack

class UploadCallBackService {

    def callback(AvThBack back) {
        UploadRecord record = UploadRecord.findByInputKey(back.inputKey)
        if (record){
            if (back.items.size() > 0){
                record.url = Constants.QINIU_DOMAIN + back.items.get(0).key
                record.lastUpdated = new Date()
                record.save()

                switch (record.fileType){
                    case Constants.FileType.pic.value:
                        if (record.ownerType == Constants.OwnerType.story.value) {
                            Story story = Story.get(record.ownerId)
                            if (story) {
                                story.pic = record.url
                                story.lastUpdated = new Date()
                                story.save(flush: true, failOnError: true)
                            }
                        } else {
                            Node node = Node.get(record.ownerId)
                            if (node) {
                                node.picUrl = record.url
                                node.lastUpdated = new Date()
                                node.save(flush: true, failOnError: true)
                            }
                        }
                        break
                    case Constants.FileType.audio.value:
                        if (record){
                            Node node = Node.get(record.ownerId)
                            if (node && node.audioId){
                                if (node.audioLoadState.equals(Constants.AvLoadState.transcoding.value)) {
                                    node.audioUrl = record.url
                                    node.audioLoadState = Constants.AvLoadState.complete.value
                                    node.lastUpdated = new Date()
                                    node.save(flush: true, failOnError: true)
                                }
                            }
                        }

                        break
                    case Constants.FileType.video.value:
                        if (record){
                            Node node = Node.get(record.ownerId)
                            if (node && node.videoId){
                                if (node.videoLoadState.equals(Constants.AvLoadState.transcoding.value)) {
                                    node.videoUrl = record.url
                                    node.videoLoadState = Constants.AvLoadState.complete.value
                                    node.lastUpdated = new Date()
                                    node.save(flush: true, failOnError: true)
                                }
                            }
                        }
                        break
                }
            }
        }
    }
}
