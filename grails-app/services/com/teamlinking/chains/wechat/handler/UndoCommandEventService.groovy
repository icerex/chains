package com.teamlinking.chains.wechat.handler

import com.teamlinking.chains.Node
import com.teamlinking.chains.Story
import com.teamlinking.chains.UserState
import com.teamlinking.chains.WechatMessage
import com.teamlinking.chains.common.Constans
import com.teamlinking.chains.common.Constans.NodeType
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
                case Constans.WechatCommand.story_image_add.key:
                    currentStory.status = 0 as Byte
                    currentStory.lastUpdated = new Date()
                    currentStory.save()

                    if (currentStory.parentId != 0){
                        userState.currentStoryId = currentStory.parentId
                    }else {
                        userState.currentStoryId = storyService.getNextStory(currentStory.id).id
                    }
                    cleanState(userState)
                    content = String.format(Constans.WECHAT_MSG_UNDO_STORY_SUCCESS, currentStory.title)
                    break
                case Constans.WechatCommand.story_add.key:
                case Constans.WechatCommand.story_sub_add.key:
                case Constans.WechatCommand.story_upate.key:
                    cleanState(userState)
                    content = Constans.WECHAT_MSG_UNDO_SUCCESS
            }
        }else {
            //撤销消息
            content = Constans.WECHAT_MSG_UNDO_FAILE
            WechatMessage lastMessage = context.get("lastMessage") as WechatMessage
            if (lastMessage){
                Node node = Node.get(lastMessage.nodeId)
                if (node && node.storyId == userState.currentStoryId){
                    lastMessage.status = 0 as Byte
                    lastMessage.lastUpdated = new Date()
                    lastMessage.save()

                    Constans.NodeType st = Constans.NodeType.pase(node.nodeType)
                    switch (lastMessage.msgType){
                        case WxConsts.XML_MSG_TEXT:
                            Constans.NodeType nt = st.pop(Constans.NodeType.text)
                            if (nt){
                                node.content = null
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_IMAGE:
                            Constans.NodeType nt = st.pop(Constans.NodeType.pic)
                            if (nt){
                                node.picUrl = null
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_VOICE:
                            Constans.NodeType nt = st.pop(Constans.NodeType.audio)
                            if (nt){
                                node.audioUrl = null
                                node.audioId = null
                                node.audioDuration = null
                                node.audioLoadState = Constans.AvLoadState.empty
                                popNode(node,nt)
                            }
                            break
                        case WxConsts.XML_MSG_VIDEO:
                            Constans.NodeType nt = st.pop(Constans.NodeType.video)
                            if (nt){
                                node.videoId = null
                                node.videoUrl = null
                                node.videoDuration = null
                                node.videoLoadState = Constans.AvLoadState.empty
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
                    content = Constans.WECHAT_MSG_UNDO_SUCCESS
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
