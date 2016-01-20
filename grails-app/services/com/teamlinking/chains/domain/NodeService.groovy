package com.teamlinking.chains.domain

import com.google.common.collect.Lists
import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.Constants
import com.teamlinking.chains.common.PageVO
import com.teamlinking.chains.vo.NodeVO
import org.apache.commons.lang.Validate
import com.teamlinking.chains.Node
import org.springframework.beans.BeanUtils

class NodeService {

    PageVO<NodeVO> list(long storyId, int max, int offset, String desc){
        Validate.isTrue(storyId > 0)
        def all = Node.createCriteria().list(max: max, offset: offset){
            eq("status", 1 as Byte)
            eq("storyId", storyId)
            order("nodeTime", desc)
        }
        List<NodeVO> list = Lists.newArrayList()
        all.each {
            NodeVO node = new NodeVO()
            BeanUtils.copyProperties(it,node)
            list << node
        }
        PageVO<NodeVO> page = new PageVO<NodeVO>()
        page.count = all.totalCount
        page.result = list
        return page
    }

    /**
     * 保存节点,5分钟内可叠加的合并成一个
     */
    Node saveByContent(UserState userState, String content) {
        Validate.notNull(userState)
        Validate.notEmpty(content)
        Node node = getLastNode(userState.currentStoryId)
        if (node && isTimeable(node.dateCreated)){
            // todo 如果是设置节点时间

            //是否可用合并
            Constants.NodeType nt = Constants.NodeType.pase(node.nodeType).stack(Constants.NodeType.text)
            if (nt){
                node.content = content
                return popNode(node,nt)
            }
        }
        Date nodeTime = new Date()
        // todo 如果是设置节点时间

        node = new Node(
                dateCreated: new Date(),
                nodeTime: nodeTime,
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
        if (node && isTimeable(node.dateCreated)){
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
        if (node && isTimeable(node.dateCreated)){
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
        if (node && isTimeable(node.dateCreated)){
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
        if (node && isTimeable(node.dateCreated)){
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
        Node.findAllByStoryIdAndStatus(storyId,1 as Byte,[max: 1, sort: "dateCreated", order: "desc"]).each {
            return it
        }
        return null
    }

    def popNode(Node node, Constants.NodeType nodeType){
        node.nodeType = nodeType.value
        node.lastUpdated = new Date()
        node.save(flush: true, failOnError: true)
    }

    boolean isTimeable(Date date){
        return (new Date()).getTime() - date.getTime() > 5*60*1000
    }
}
