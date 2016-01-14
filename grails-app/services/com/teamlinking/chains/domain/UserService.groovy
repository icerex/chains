package com.teamlinking.chains.domain

import com.teamlinking.chains.User
import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.Constans
import org.apache.commons.lang.Validate

class UserService {

    User get(long id){
        Validate.isTrue(id > 0)
        User.findById(id)
    }

    User get(String openId){
        Validate.notEmpty(openId)
        User.findByOpenId(openId)
    }
}
