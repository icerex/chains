package com.teamlinking.chains.wechat.handler

import com.google.common.collect.Lists
import com.teamlinking.chains.Story
import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.vo.StorySimpleVO
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage

/**
 * 公众号菜单当前主题按钮点击事件
 */
class MenuCurrentStoryClickEventService implements WxMpMessageHandler{

    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        Story currentStory = context.get("currentStory") as Story
        UserState userState = context.get("userState") as UserState
        String str = "无"
        def cm = Constants.WechatCommand.pase(userState.command)
        if (cm){
            str = cm.value
        }
        List<StorySimpleVO> storys = findStorys(currentStory.uid,currentStory.parentId,currentStory.id)
        String content = String.format(Constants.WECHAT_MSG_CURRENT_STORY,getStoryString(storys,""),str)
        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

    List<StorySimpleVO> findStorys(long uid, long parentId, long currentId){
        List<StorySimpleVO> vos = Lists.newArrayList()
        Story.findAllByParentIdAndUidAndStatus(parentId,uid,1 as Byte,[max: 10, sort: "dateCreated", order: "asc"]).each {
            StorySimpleVO vo = new StorySimpleVO()
            vo.id = it.id
            if (it.id == currentId){
                vo.title = "#"+it.title+"#"
            }else {
                vo.title = it.title
            }
            vo.subs = findStorys(uid,it.id,currentId)
            vos << vo
        }
        return vos
    }

    String getStoryString(List<StorySimpleVO> storys,String temp){
        StringBuilder sb = new StringBuilder()
        storys.each {
            sb.append(temp+it.title+"\n")
            sb.append(getStoryString(it.subs,"|-"))
        }
        return sb.toString()
    }
}
