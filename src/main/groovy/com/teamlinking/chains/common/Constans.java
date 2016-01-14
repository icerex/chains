package com.teamlinking.chains.common;

/**
 * Created by admin on 16/1/14.
 */
public interface Constans {

    enum WechatMenu {

        currentStory("CURRENT_STORY","查看主题"),
        updateStory("UPDATE_STORY","修改主题"),
        addStory("ADD_STORY","新增主题"),
        addSubStory("ADD_SUB_STORY","新增子主题"),
        nextStory("NEXT_STORY","切换主题"),
        backParent("BACK_PARENT","回到上级主题"),
        undo("UNDO","撤销节点|操作");


        String key;
        String value;
        WechatMenu(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    enum WechatCommand{
        //菜单命令
        story_upate("story_upate","修改主题"),
        story_add("story_add","新增主题"),
        story_sub_add("story_sub_add","新增子主题"),

        //流程命令
        story_image_add("story_image_add","添加主题背景");

        String key;
        String value;
        WechatCommand(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    String WECHAT_MSG_SUBSCRIBE = "你好!欢迎来到记忆的空间,你可以回复\"帮助\"两字查看使用说明";
    String WECHAT_MSG_CURRENT_STORY = "当前主题:%s";
    String WECHAT_MSG_NEXT_STORY = "切换成功,当前主题是:%s";
    String WECHAT_MSG_NEXT_STORY_FAILE = "没有主题可以切换,当前主题是:%s";
    String WECHAT_MSG_BACK_PARENT = "返回上级成功,当前主题是:%s";
    String WECHAT_MSG_BACK_PARENT_FAILE = "该主题已经是最上级,当前主题是:%s";

    String WECHAT_MSG_UPDATE_STORY_BEFORE = "你正在修改主题:%s,回复文字将修改标题(10个中文以内),回复图片将修改主题背景";

    String WECHAT_MSG_ADD_STORY_BEFORE = "请输入标题10个中文以内";
    String WECHAT_MSG_ADD_SUB_STORY_BEFORE = "你正在主题:%s中增加子主题,请输入标题10个中文以内";

}
