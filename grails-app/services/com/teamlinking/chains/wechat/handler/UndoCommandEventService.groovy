package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Node
import com.teamlinking.chains.Story
import com.teamlinking.chains.UserState
import com.teamlinking.chains.WechatMessage
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.common.Constants.NodeType
import com.teamlinking.chains.domain.StoryService
import grails.transaction.Transactional
import me.chanjar.weixin.common.api.WxConsts
import me.chanjar.weixin.common.exception.WxErrorException
import me.chanjar.weixin.common.session.WxSessionManager
import me.chanjar.weixin.mp.api.WxMpMessageHandler
import me.chanjar.weixin.mp.api.WxMpService
import me.chanjar.weixin.mp.bean.WxMpXmlMessage
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage

/**
 * 撤销|取消命令事件
 */
class UndoCommandEventService implements WxMpMessageHandler{

    StoryService storyService

    @Override
    @Transactional
    WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        Story currentStory = context.get("currentStory") as Story
        UserState userState = context.get("userState") as UserState
        String content = null
        if (userState.command){
            //撤销命令
            switch (userState.command){
                case Constants.WechatCommand.story_image_add.key:
                    currentStory.status = 0 as Byte
                    currentStory.lastUpdated = new Date()
                    currentStory.save()

                    if (currentStory.parentId != 0){
                        userState.currentStoryId = currentStory.parentId
                    }else {
                        userState.currentStoryId = storyService.getNextStory(currentStory.uid,currentStory.id,currentStory.parentId).id
                    }
                    cleanState(userState)
                    content = String.format(Constants.WECHAT_MSG_UNDO_STORY_SUCCESS, currentStory.title)
                    break
                case Constants.WechatCommand.story_add.key:
                case Constants.WechatCommand.story_sub_add.key:
                case Constants.WechatCommand.story_upate.key:
                    cleanState(userState)
                    content = Constants.WECHAT_MSG_UNDO_SUCCESS
            }
        }else {
            //撤销消息
            content = Constants.WECHAT_MSG_UNDO_FAILE
            WechatMessage lastMessage = context.get("lastMessage") as WechatMessage
            if (lastMessage){
                Node node = Node.get(lastMessage.nodeId)
                if (node && node.storyId == userState.currentStoryId){
                    lastMessage.status = 0 as Byte
                    lastMessage.lastUpdated = new Date()
                    lastMessage.save()

                    Constants.NodeType st = Constants.NodeType.pase(node.nodeType)
                    switch (lastMessage.msgType){
                        case WxConsts.XML_MSG_TEXT:
                            Constants.NodeType nt = st.pop(Constants.NodeType.text)
                            if (nt){
                                node.content = null
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_IMAGE:
                            Constants.NodeType nt = st.pop(Constants.NodeType.pic)
                            if (nt){
                                node.picUrl = null
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_VOICE:
                            Constants.NodeType nt = st.pop(Constants.NodeType.audio)
                            if (nt){
                                node.audioUrl = null
                                node.audioId = null
                                node.audioDuration = null
                                node.audioLoadState = Constants.AvLoadState.empty
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_VIDEO:
                            Constants.NodeType nt = st.pop(Constants.NodeType.video)
                            if (nt){
                                node.videoId = null
                                node.videoUrl = null
                                node.videoDuration = null
                                node.videoLoadState = Constants.AvLoadState.empty
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_LOCATION:
                            node.latitude = null
                            node.longitude = null
                            node.locationLab = null
                            popNode(node,st)
                            break
                    }
                    content = Constants.WECHAT_MSG_UNDO_SUCCESS
                }
            }
        }

        return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.toUserName).toUser(wxMessage.fromUserName).build()
    }

    void cleanState(UserState userState){
        userState.command = null
        userState.lastUpdated = new Date()
        userState.save(flush: true, failOnError: true)
    }

    void popNode(Node node,NodeType nodeType){
        node.nodeType = nodeType.value
        node.lastUpdated = new Date()
        node.save(flush: true, failOnError: true)
    }
}
