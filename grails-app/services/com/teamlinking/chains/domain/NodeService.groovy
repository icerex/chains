package com.teamlinking.chains.domain

import com.google.common.collect.Lists
import com.teamlinking.chains.Story
import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.Base32Util
import com.teamlinking.chains.common.CommonUtil
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.vo.PageVO
import com.teamlinking.chains.vo.StoryNodeVO
import grails.config.Config
import org.apache.commons.lang.Validate
import com.teamlinking.chains.Node
import org.springframework.beans.BeanUtils

class NodeService {

    /**
     * 分页获取
     * @param storyId
     * @param max
     * @param offset
     * @param desc
     * @param config
     * @return
     */
    PageVO<StoryNodeVO> list(long storyId, int max, int offset, String desc, Config config){
        Validate.isTrue(storyId > 0)
        List<Node> all = Node.createCriteria().list(max: max, offset: offset){
            eq("status", 1 as Byte)
            eq("storyId", storyId)
            order("nodeTime", desc)
        }

        Date end = null

        List<StoryNodeVO> list = Lists.newArrayList()
        all.each {
            StoryNodeVO node = new StoryNodeVO()
            BeanUtils.copyProperties(it,node)
            node.pics << it.picUrl
            list << node

            end = it.nodeTime
        }
        String protocol = config.getProperty("protocol")
        String domain = config.getProperty("domain")
        //加上子主题
        List<Story> stories = Story.createCriteria().list(){
            eq("status", 1 as Byte)
            eq("parentId", storyId)
            if (end) {
                if ("asc".equals(desc)) {
                    ge("dateCreated", end)
                } else {
                    gt("dateCreated", end)
                }
            }
            order("dateCreated", desc)
        }
        stories.each {
            StoryNodeVO node = new StoryNodeVO()
            node.setSub(true)
            node.content = it.title
            node.id = it.id
            node.nodeTime = it.dateCreated
            node.nodeType = Constants.NodeType.textAndPic.value
            node.subUrl = protocol+domain+"/1/story/"+Base32Util.enCode32(""+it.id)
            def pics = getPics(it.id)
            if (pics.size() > 0) {
                node.pics = pics
            }else {
                if (it.pic) {
                    node.pics << it.pic
                }
            }

            boolean isAdded = false
            for (int i=0;i < list.size();i++){
                if (list.get(i).nodeTime.after(node.nodeTime)){
                    list.add(i,node)
                    isAdded = true
                }
            }
            if (!isAdded){
                list << node
            }
        }

        PageVO<StoryNodeVO> page = new PageVO<StoryNodeVO>()
        page.count = all.totalCount
        page.result = list
        return page
    }

    /**
     * 获取图片集合
     * @param storyId
     * @return
     */
    List<String> getPics(long storyId){
        List<String> list = Lists.newArrayList()
        Node.findAllByStoryIdAndStatusAndPicUrlIsNotNull(storyId,1 as Byte,[max: 9, sort: "dateCreated", order: "desc"]).each {
            list << it.picUrl
        }
        return list
    }

    /**
     * 设置节点时间,5分钟内
     * @param userState
     * @param date
     * @return
     */
    Node setNodeTime(UserState userState,Date date){
        Validate.notNull(userState)
        Validate.notEmpty(date)
        Node node = getLastNode(userState.currentStoryId)
        if (node && CommonUtil.isFiveMinuteable(node.dateCreated)){
            node.nodeTime = date
            return popNode(node,Constants.NodeType.pase(node.nodeType))
        }
        return node
    }

    /**
     * 保存节点,5分钟内可叠加的合并成一个
     */
    Node saveByContent(UserState userState, String content) {
        Validate.notNull(userState)
        Validate.notEmpty(content)
        Node node = getLastNode(userState.currentStoryId)
        if (node && CommonUtil.isFiveMinuteable(node.dateCreated)){
            //是否可用合并
            Constants.NodeType nt = Constants.NodeType.pase(node.nodeType).stack(Constants.NodeType.text)
            if (nt){
                node.content = content
                return popNode(node,nt)
            }
        }
        node = new Node(
                dateCreated: new Date(),
                nodeTime: new Date(),
                uid: userState.uid,
                storyId: userState.currentStoryId,
                content: content,
                longitude: userState.longitude,
                latitude: userState.latitude
        )
        return popNode(node,Constants.NodeType.text)
    }

    /**
     * 保存节点,5分钟内可叠加的合并成一个
     */
    Node saveByImage(UserState userState, String picUrl) {
        Validate.notNull(userState)
        Validate.notEmpty(picUrl)
        Node node = getLastNode(userState.currentStoryId)
        if (node && CommonUtil.isFiveMinuteable(node.dateCreated)){
            //是否可用合并
            Constants.NodeType nt = Constants.NodeType.pase(node.nodeType).stack(Constants.NodeType.pic)
            if (nt){
                node.picUrl = picUrl
                return popNode(node,nt)
            }
        }
        node = new Node(
                dateCreated: new Date(),
                nodeTime: new Date(),
                uid: userState.uid,
                storyId: userState.currentStoryId,
                picUrl: picUrl,
                longitude: userState.longitude,
                latitude: userState.latitude
        )
        return popNode(node,Constants.NodeType.pic)
    }

    /**
     * 保存节点,5分钟内可叠加的合并成一个
     */
    Node saveByAudio(UserState userState, String mediaId) {
        Validate.notNull(userState)
        Validate.notEmpty(mediaId)
        Node node = getLastNode(userState.currentStoryId)
        if (node && CommonUtil.isFiveMinuteable(node.dateCreated)){
            //是否可用合并
            Constants.NodeType nt = Constants.NodeType.pase(node.nodeType).stack(Constants.NodeType.audio)
            if (nt){
                node.audioId = mediaId
                node.audioLoadState = Constants.AvLoadState.uploading.value
                return popNode(node,nt)
            }
        }
        node = new Node(
                dateCreated: new Date(),
                nodeTime: new Date(),
                uid: userState.uid,
                storyId: userState.currentStoryId,
                audioId: mediaId,
                audioLoadState:Constants.AvLoadState.uploading.value,
                longitude: userState.longitude,
                latitude: userState.latitude
        )
        return popNode(node,Constants.NodeType.audio)
    }

    /**
     * 保存节点,5分钟内可叠加的合并成一个
     */
    Node saveByVideo(UserState userState, String mediaId) {
        Validate.notNull(userState)
        Validate.notEmpty(mediaId)
        Node node = getLastNode(userState.currentStoryId)
        if (node && CommonUtil.isFiveMinuteable(node.dateCreated)){
            //是否可用合并
            Constants.NodeType nt = Constants.NodeType.pase(node.nodeType).stack(Constants.NodeType.video)
            if (nt){
                node.videoId = mediaId
                node.videoLoadState = Constants.AvLoadState.uploading.value
                return popNode(node,nt)
            }
        }
        node = new Node(
                dateCreated: new Date(),
                nodeTime: new Date(),
                uid: userState.uid,
                storyId: userState.currentStoryId,
                videoId: mediaId,
                videoLoadState: Constants.AvLoadState.uploading.value,
                longitude: userState.longitude,
                latitude: userState.latitude
        )
        return popNode(node,Constants.NodeType.video)
    }

    /**
     * 保存节点,5分钟内可叠加的合并成一个
     */
    Node saveByLocation(UserState userState,Double longitude,Double latitude,String locationLab){
        Validate.notNull(userState)
        Validate.notNull(longitude)
        Validate.notNull(latitude)
        Validate.notEmpty(locationLab)

        Node node = getLastNode(userState.currentStoryId)
        if (node && CommonUtil.isFiveMinuteable(node.dateCreated)){
            //是否可用合并
            if (node.locationLab == null){
                node.locationLab = locationLab
                node.latitude = latitude
                node.longitude = longitude
                node.lastUpdated = new Date()
                return node.save(flush: true, failOnError: true)
            }

        }
        node = new Node(
                dateCreated: new Date(),
                lastUpdated: new Date(),
                nodeTime: new Date(),
                uid: userState.uid,
                storyId: userState.currentStoryId,
                longitude: longitude,
                latitude: latitude,
                locationLab: locationLab
        )
        return node.save(flush: true, failOnError: true)
    }

    /**
     * 获取当前主题最后创建的节点
     * @param storyId
     * @return
     */
    Node getLastNode(long storyId){
        Node node = null
        Node.findAllByStoryIdAndStatus(storyId,1 as Byte,[max: 1, sort: "dateCreated", order: "desc"]).each {
            node = it
        }
        return node
    }

    def popNode(Node node, Constants.NodeType nodeType){
        node.nodeType = nodeType.value
        node.lastUpdated = new Date()
        node.save(flush: true, failOnError: true)
    }
}
