package com.teamlinking.chains.domain

import com.google.common.collect.Lists
import com.teamlinking.chains.Node
import com.teamlinking.chains.Story
import com.teamlinking.chains.User
import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.Base32Util
import com.teamlinking.chains.vo.StoryVO
import grails.config.Config
import org.apache.commons.lang.Validate


class StoryService {

    Story get(long id){
        Validate.isTrue(id > 0)
        Story.get(id)
    }

    List<StoryVO> getAll(long uid, Config config){
        Validate.isTrue(uid > 0)
        return findStorys(uid,0L,config)
    }

    List<StoryVO> findStorys(long uid,long parentId,Config config){
        List<StoryVO> storyVOs = Lists.newArrayList()
        Story.findAllByParentIdAndUidAndStatus(parentId,uid,1 as Byte,[sort: "dateCreated", order: "asc"]).each {
            StoryVO vo = new StoryVO()
            vo.id = it.id
            vo.pic = it.pic
            vo.title = it.title
            vo.order = storyVOs.size()
            String protocol = config.getProperty("protocol")
            String domain = config.getProperty("domain")
            vo.url = protocol+domain+"/1/story/"+Base32Util.enCode32(""+it.id)
            vo.date = getDate(it.id,it.dateCreated,it.lastUpdated)
            vo.subs = findStorys(uid,it.id,config)
            storyVOs << vo
        }
        return storyVOs
    }

    String getDate(long storyId,Date create,Date update){
        Validate.isTrue(storyId > 0)
        Validate.notNull(create)
        Validate.notNull(update)
        Date start = create
        Node.findAllByStoryIdAndStatus(storyId,1 as Byte,[max: 1, sort: "nodeTime", order: "asc"]).each {
            start = it.nodeTime
        }

        Date end = update
        Node.findAllByStoryIdAndStatus(storyId,1 as Byte,[max: 1, sort: "nodeTime", order: "desc"]).each {
            end = it.nodeTime
        }
        return dateAndDate(start,end)
    }

    String dateAndDate(Date start,Date end){
        Calendar cal = Calendar.getInstance();
        cal.setTime(start);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(end);

        if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
            if (cal.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)){
                return cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH)+1)+"月"
            }else {
                return (cal.get(Calendar.MONTH)+1)+"月~"+(cal2.get(Calendar.MONTH)+1)+"月"
            }
        }else{
            return cal.get(Calendar.YEAR)+"~"+cal2.get(Calendar.YEAR)
        }
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
     * 下一个主题,如果是最后一个那下一个为第一个,如果该父主题下只有一个则返回null
     * @param currentId
     */
    Story getNextStory(long uid,long currentId,long parentId){
        Validate.isTrue(currentId > 0)
        Story next = null
        if (parentId == -1L){
            Story story = get(currentId)
            parentId = story.parentId
        }
        Story.createCriteria().list(max: 1) {
            eq("parentId", parentId)
            gt("id", currentId)
            eq("uid",uid)
            eq("status",1 as Byte)
        }.each {
            next = it
        }
        if (next == null){
            Story.findAllByParentIdAndStatus(parentId,1 as Byte,[max: 1, sort: "dateCreated", order: "asc"]).each {
                if (it.id != currentId){
                    next = it
                }
            }
        }
        return next
    }

    /**
     * 子主题
     * @param currentId
     */
    Story getSonStory(long uid,long currentId){
        Story.findAllByUidAndParentIdAndStatus(uid,currentId,1 as Byte,[max: 1, sort: "dateCreated", order: "asc"]).each {
            return it
        }
        return null
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
