package com.teamlinking.chains.domain

import com.teamlinking.chains.Story
import com.teamlinking.chains.User
import com.teamlinking.chains.UserState
import com.teamlinking.chains.vo.StoryVO
import org.apache.commons.lang.Validate


class StoryService {

    Story get(long id){
        Story.get(id)
    }

    List<StoryVO> getAllStory(long uid){

    }

    /**
     * 当前主题
     * @param openId
     * @return
     */
    Story getCurrentStory(String openId) {
        User user = User.findByOpenId(openId)
        UserState userState = UserState.findByUid(user.id)
        get(userState.currentStoryId)
    }

    /**
     * 下一个主题,先返回子主题,没有子主题返回当前父主题下的主题,如果是最后一个那下一个为第一个,如果该父主题下只有一个则返回null
     * @param currentId
     */
    Story getNextStory(long currentId,long parentId){
        Story next = null
        Story.findAllByParentId(currentId,[max: 1, sort: "dateCreated", order: "asc"]).each {
            next = it
        }
        if (next) {
            if (parentId == -1){
                Story story = get(currentId)
                parentId = story.parentId
            }
            Story.createCriteria().list(max: 1) {
                eq("parentId", parentId)
                gt("id", currentId)
            }.each {
                next = it
            }
        }
        if (next == null){
            Story.findAllByParentId(parentId,[max: 1, sort: "dateCreated", order: "asc"]).each {
                if (it.id != currentId){
                    next = it
                }
            }
        }
        return next
    }

    /**
     * 父主题
     * @param currentId
     * @return
     */
    Story getParentStory(long currentId){
        Story story = get(currentId)
        if (story.parentId > 0){
            return get(story.parentId)
        }
        return null
    }

    /**
     * 初始化主题数据
     */
    Story initMasterStory(long uid){
        List<Story> list =  Story.findAllByUidAndParentId(uid,0,[max: 1])
        if (list.size() == 0){
            Story story = new Story(
                    uid: uid,
                    dateCreated: new Date(),
                    title: "人生就是一场戏",
                    lastUpdated: new Date()
            )
            return story.save(flush: true,failOnError: true)
        }
        return list.get(0)
    }
}
