package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Story
import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.domain.StoryService
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage

/**
 * 公众号命令类型菜单点击事件
 */
class MenuCommandClickEventService implements WxMpMessageHandler{

    StoryService storyService

    @Override
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        Story currentStory = context.get("currentStory") as Story
        UserState userState = context.get("userState") as UserState
        String content = null

        if (userState.command){
            //如果在流程命令中,不能菜单执行命令
            switch (userState.command){
                case Constants.WechatCommand.story_image_add.key:
                    content = String.format(Constants.WECHAT_MSG_ADD_STORY_IMAGE, currentStory.title)
                    break
            }
        }
        if (content == null) {
            switch (wxMessage.eventKey) {
                case Constants.WechatMenu.updateStory.key:
                    userState.command = Constants.WechatCommand.story_upate.key
                    userState.lastUpdated = new Date()
                    userState.save(flush: true, failOnError: true)
                    content = String.format(Constants.WECHAT_MSG_UPDATE_STORY_BEFORE, currentStory.title)
                    break
                case Constants.WechatMenu.addStory.key:
                    userState.command = Constants.WechatCommand.story_add.key
                    userState.lastUpdated = new Date()
                    userState.save(flush: true, failOnError: true)
                    content = Constants.WECHAT_MSG_ADD_STORY_BEFORE
                    break
                case Constants.WechatMenu.addSubStory.key:
                    userState.command = Constants.WechatCommand.story_sub_add.key
                    userState.lastUpdated = new Date()
                    userState.save(flush: true, failOnError: true)
                    content = String.format(Constants.WECHAT_MSG_ADD_SUB_STORY_BEFORE, currentStory.title)
                    break
                case Constants.WechatMenu.nextStory.key:
                    Story next = storyService.getNextStory(currentStory.uid,currentStory.id,currentStory.parentId)
                    if (next) {
                        userState.command = null
                        userState.lastUpdated = new Date()
                        userState.currentStoryId = next.id
                        userState.save(flush: true, failOnError: true)
                        content = String.format(Constants.WECHAT_MSG_NEXT_STORY, next.title)
                    } else {
                        content = String.format(Constants.WECHAT_MSG_NEXT_STORY_FAILE, currentStory.title)
                    }
                    break
                case Constants.WechatMenu.sonStory.key:
                    Story son = storyService.getSonStory(currentStory.uid,currentStory.id)
                    if (son) {
                        userState.command = null
                        userState.lastUpdated = new Date()
                        userState.currentStoryId = son.id
                        userState.save(flush: true, failOnError: true)
                        content = String.format(Constants.WECHAT_MSG_NEXT_STORY, son.title)
                    } else {
                        content = String.format(Constants.WECHAT_MSG_NEXT_STORY_FAILE, currentStory.title)
                    }
                    break
                case Constants.WechatMenu.backParent.key:
                    content = String.format(Constants.WECHAT_MSG_BACK_PARENT_FAILE, currentStory.title)
                    if (currentStory.parentId > 0) {
                        Story parent = storyService.get(currentStory.parentId)
                        if (parent) {
                            userState.command = null
                            userState.lastUpdated = new Date()
                            userState.currentStoryId = parent.id
                            userState.save(flush: true, failOnError: true)
                            content = String.format(Constants.WECHAT_MSG_BACK_PARENT, parent.title)
                        }
                    }
                    break

            }
        }
        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }
}
