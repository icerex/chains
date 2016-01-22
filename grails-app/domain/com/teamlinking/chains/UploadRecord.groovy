package com.teamlinking.chains

class UploadRecord {

    Long id

    Date dateCreated

    Date lastUpdated

    Byte status = 1 as Byte
    //文件类型
    Integer fileType
    //文件唯一标示
    String key
    //所属者,主题\节点 ID
    Long ownerId
    //所属者类型,主题\节点
    Integer ownerType

    String url

    String params

    static constraints = {
        dateCreated nullable: false, blank: false
        status inList: [1 as byte, 0 as byte]
        fileType nullable: false, blank:false
        key nullable: false, blank:false
        ownerId nullable: false, blank:false
        ownerType nullable: false, blank:false
        lastUpdated nullable: true, blank:true
        url nullable: true, blank: true
        params nullable: true, blank: true
    }

    static mapping = {
        table('t_upload_queue')
        version(false)
        id generator: 'identity'
        key unique: true
    }
}
