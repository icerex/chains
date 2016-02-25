package com.teamlinking.chains

class Node {

    Long id

    Date dateCreated

    Date lastUpdated

    Byte status = 1 as Byte

    Date nodeTime

    Long uid

    Long storyId

    Integer nodeType

    String content

    String picUrl

    //视频id
    String videoId
    //音频地址
    String videoUrl
    //视频的长度,单位s
    Double videoDuration
    //视频上传状态
    Byte videoLoadState = 0 as Byte

    String locationLab
    //纬度
    Double latitude
    //经度
    Double longitude

    static constraints = {
        uid nullable: false, blank: false
        dateCreated nullable: false, blank: false
        status inList: [1 as byte, 0 as byte]
        nodeTime nullable: false, blank: false
        storyId nullable: false, blank:false
        nodeType nullable: false, blank:false
        lastUpdated nullable: true, blank:true
        content nullable: true, blank: true
        picUrl nullable: true, blank:true
        videoId nullable: true, blank:true
        videoUrl nullable: true, blank:true
        videoDuration nullable: true, blank:true
        videoLoadState nullable: true, blank:true
        longitude nullable: true, blank:true
        locationLab nullable: true, blank:true
        latitude nullable: true, blank:true
        longitude nullable: true, blank:true
    }

    static mapping = {
        table('t_node')
        version(false)
        id generator: 'identity'
    }
}
