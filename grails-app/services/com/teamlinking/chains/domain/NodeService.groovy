package com.teamlinking.chains.domain

import com.teamlinking.chains.UserState
import com.teamlinking.chains.common.Constans
import org.apache.commons.lang.Validate
import com.teamlinking.chains.Node

class NodeService {

    boolean saveByImage(UserState userState, String picUrl) {
        Validate.notNull(userState)
        Validate.notEmpty(picUrl)
        Node node = getLastNode(userState.currentStoryId)
        if (node){
            Constans.NodeType nt = Constans.NodeType.pase(node.nodeType).stack(Constans.NodeType.pic)
            if (nt){
                node.picUrl = picUrl
                popNode(node,nt)
                return true
            }
            return false
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
        popNode(node,Constans.NodeType.pic)
        return true
    }

    /**
     * 获取当前主题最后节点
     * @param storyId
     * @return
     */
    Node getLastNode(long storyId){
        Node.findAllByStoryIdAndStatus(storyId,1 as Byte,[max: 1, sort: "nodeTime", order: "desc"]).each {
            return it
        }
        return null
    }

    void popNode(Node node, Constans.NodeType nodeType){
        node.nodeType = nodeType.value
        node.lastUpdated = new Date()
        node.save(flush: true, failOnError: true)
    }
}
