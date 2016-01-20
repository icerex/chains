package com.teamlinking.chains.domain

import com.teamlinking.chains.User
import com.teamlinking.chains.UserState
import me.chanjar.weixin.mp.bean.result.WxMpUser
import org.apache.commons.lang.Validate
import org.springframework.beans.BeanUtils

class UserService {

    User get(long id){
        Validate.isTrue(id > 0)
        User.findById(id)
    }

    User get(String openId){
        Validate.notEmpty(openId)
        User.findByOpenId(openId)
    }

    /**
     * 初始化用户数据
     */
    User initUser(WxMpUser wxMpUser){
        Validate.notNull(wxMpUser)
        User user = User.findByOpenId(wxMpUser.openId)
        if (user == null){
            if (wxMpUser.unionId) {
                user = User.findByUnionId(wxMpUser.unionId)
            }
            if (user){
                user.openId = wxMpUser.openId
            }else {
                user = new User(
                        dateCreated: new Date()
                )
            }
        }
        BeanUtils.copyProperties(wxMpUser,user)
        user.lastUpdated = new Date()
        user.save(flush: true,failOnError: true)
    }

    /**
     * 初始化用户状态
     */
    def initState(long uid,long storyId){
        UserState state = UserState.findByUid(uid)
        if (state == null){
            state = new UserState(
                    uid: uid,
                    dateCreated: new Date(),
                    lastUpdated: new Date(),
                    currentStoryId: storyId

            )
            state.save(flush: true,failOnError: true)
        }
    }
}
