package com.teamlinking.chains;

import com.teamlinking.chains.common.Constans;

/**
 * Created by admin on 16/1/15.
 */
public class Test {
    public static void main(String args[]){
        System.out.println(Constans.NodeType.text.stack(Constans.NodeType.pic));
        System.out.println(Constans.NodeType.pic.stack(Constans.NodeType.pic));
        System.out.println(Constans.NodeType.pic.stack(Constans.NodeType.textAndAudio));
        System.out.println(Constans.NodeType.text.stack(Constans.NodeType.textAndVideo));
        System.out.println(Constans.NodeType.textAndAudio.stack(Constans.NodeType.textAndVideo));

        System.out.println("---------------------");

        System.out.println(Constans.NodeType.textAndPic.pop(Constans.NodeType.text));
        System.out.println(Constans.NodeType.textAndPic.pop(Constans.NodeType.pic));
        System.out.println(Constans.NodeType.textAndVideo.pop(Constans.NodeType.video));
        System.out.println(Constans.NodeType.textAndVideo.pop(Constans.NodeType.audio));
        System.out.println(Constans.NodeType.textAndAudio.stack(Constans.NodeType.textAndVideo));
        System.out.println(Constans.NodeType.textAndAudio.stack(Constans.NodeType.textAndPic));
    }
}
