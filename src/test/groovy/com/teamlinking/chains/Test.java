package com.teamlinking.chains;

import com.teamlinking.chains.common.Base32Util;
import com.teamlinking.chains.common.CommonUtil;
import com.teamlinking.chains.common.Constants;

/**
 * Created by admin on 16/1/15.
 */
public class Test {
    public static void main(String args[]){
        System.out.println(Constants.NodeType.text.stack(Constants.NodeType.pic));
        System.out.println(Constants.NodeType.text.stack(Constants.NodeType.audio));
        System.out.println(Constants.NodeType.text.stack(Constants.NodeType.video));
        System.out.println(Constants.NodeType.audio.stack(Constants.NodeType.pic));
        System.out.println(Constants.NodeType.audio.stack(Constants.NodeType.video));
        System.out.println(Constants.NodeType.pic.stack(Constants.NodeType.pic));
        System.out.println(Constants.NodeType.pic.stack(Constants.NodeType.textAndAudio));
        System.out.println(Constants.NodeType.text.stack(Constants.NodeType.textAndVideo));
        System.out.println(Constants.NodeType.textAndAudio.stack(Constants.NodeType.textAndVideo));

        System.out.println("---------------------");

        System.out.println(Constants.NodeType.textAndPic.pop(Constants.NodeType.text));
        System.out.println(Constants.NodeType.textAndPic.pop(Constants.NodeType.pic));
        System.out.println(Constants.NodeType.textAndVideo.pop(Constants.NodeType.video));
        System.out.println(Constants.NodeType.textAndVideo.pop(Constants.NodeType.audio));
        System.out.println(Constants.NodeType.textAndAudio.pop(Constants.NodeType.textAndVideo));
        System.out.println(Constants.NodeType.textAndAudio.pop(Constants.NodeType.textAndPic));
        System.out.println(Constants.NodeType.audioAndVideo.pop(Constants.NodeType.textAndVideo));
        System.out.println(Constants.NodeType.audioAndPic.pop(Constants.NodeType.textAndPic));

        System.out.println("---------------------");

        System.out.println(Base32Util.enCode32("1"));

        System.out.println(String.format(Constants.WECHAT_MSG_ADD_STORY_IMAGE, "dadf"));

        System.out.println("---------------------");

        System.out.println("date:"+CommonUtil.matcherDate("#2013-01-31#asdfasdfsadfas撒地方"));
        System.out.println("date:"+CommonUtil.matcherDate("#2013-01-31#"));
        System.out.println("date:"+CommonUtil.matcherDate("#2013年1月31日#"));
        System.out.println("date:"+CommonUtil.matcherDate("#2013年1月#"));
        System.out.println("date:"+CommonUtil.matcherDate("#2013年132#"));

        System.out.println("---------------------");

        System.out.println("uuid:"+CommonUtil.uploadPrimaryKey("sdas".getBytes()));
    }
}
