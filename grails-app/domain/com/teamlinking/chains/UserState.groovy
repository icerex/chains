package com.teamlinking.chains

class UserState {

    Long id

    Long uid

    Date dateCreated

    Date lastUpdated

    Long currentStoryId

    String command

    static constraints = {
        uid nullable: false, blank: false
        dateCreated nullable: false, blank: false
        currentStoryId nullable: false, blank: false
        lastUpdated nullable: true, blank:true
        command nullable: true, blank:true
    }

    static mapping = {
        table('t_user_state')
        version(false)
        id generator: 'identity'
        uid unique: true
    }
}
