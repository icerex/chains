package com.teamlinking.chains.common;

/**
 * Created by admin on 16/1/14.
 */
public interface Constants {

    enum WechatMenu {

        currentStory("CURRENT_STORY","查看主题"),
        updateStory("UPDATE_STORY","修改主题"),
        addStory("ADD_STORY","新增主题"),
        addSubStory("ADD_SUB_STORY","新增子主题"),
        nextStory("NEXT_STORY","切换主题"),
        backParent("BACK_PARENT","回到上级主题"),
        undo("UNDO","撤销操作|消息");


        public String key;
        public String value;
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
        story_image_add("story_image_add","添加主题配图");

        public String key;
        public String value;
        WechatCommand(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    enum NodeType{
        text("文字",1),
        pic("图片",8),
        audio("音频",16),
        video("视频",32),
        textAndPic("图文",9),
        textAndAudio("文字+音频",17),
        textAndVideo("文字+视频",33),
        audioAndPic("图音",24),
        audioAndVideo("音频+视频",48);

        public String key;
        public int value;
        NodeType(String key, int value) {
            this.key = key;
            this.value = value;
        }

        /**
         * 类型叠加,叠加失败返回 null
         * @param nt 类型
         * @return
         */
        public NodeType stack(NodeType nt){
            if ((this.value & nt.value) == 0){
                return pase(this.value | nt.value);
            }
            return null;
        }

        /**
         * 类型删减,删减失败返回 null
         * @param nt 类型
         * @return
         */
        public NodeType pop(NodeType nt){
            if ((this == textAndPic || this == textAndAudio || this == textAndVideo
                    || this == audioAndPic || this == audioAndVideo)
                    && (nt == text || nt == pic || nt == audio || nt == video)){
                return pase(this.value & nt.value);
            }
            return null;
        }

        public static NodeType pase(int value){
            if (value > 0){
                for (NodeType type : NodeType.values()){
                    if (type.value == value){
                        return type;
                    }
                }
            }
            return null;
        }
    }

    enum AvLoadState{
        empty("无",Byte.valueOf("0")),
        uploading("上传中",Byte.valueOf("1")),
        transcoding("转码中",Byte.valueOf("2")),
        complete("完成",Byte.valueOf("9"));

        public String key;
        public Byte value;
        AvLoadState(String key, Byte value) {
            this.key = key;
            this.value = value;
        }

    }

    String ENCODE_32_SALT = "8esflhw9y2435kcr6xqinojzm7pgd1tv";

    String WECHAT_MSGTYPE_TEXT_COMMAND_UNDO = "取消";

    /** 微信消息返回 **/
    String WECHAT_MSG_SUBSCRIBE = "你好!欢迎来到记忆的空间,你可以回复\'帮助\'两字查看使用说明";
    String WECHAT_MSG_CURRENT_STORY = "当前主题:%s";
    String WECHAT_MSG_NEXT_STORY = "切换完成,当前主题是\'%s\'";
    String WECHAT_MSG_NEXT_STORY_FAILE = "没有主题可以切换,当前主题是\'%s\'";
    String WECHAT_MSG_BACK_PARENT = "返回上级成功,当前主题是\'%s\'";
    String WECHAT_MSG_BACK_PARENT_FAILE = "该主题已经是最上级,当前主题是\'%s\'";

    String WECHAT_MSG_UPDATE_STORY_BEFORE = "你正在修改主题\'%s\',回复文字将修改标题(10个中文以内),回复图片将修改主题配图";
    String WECHAT_MSG_UPDATE_STORY_IMAGE_AFTER = "主题\'s%\'配图修改成功";

    String WECHAT_MSG_ADD_STORY_BEFORE = "请输入标题10个中文以内";
    String WECHAT_MSG_ADD_SUB_STORY_BEFORE = "你正在主题\'%s\'中增加子主题,请输入标题10个中文以内";
    String WECHAT_MSG_ADD_STORY_IMAGE = "请上传主题\'s%\'配图.如你想删除该主题,回复文字\'取消\'或使用操作菜单中\'撤销\'按钮";
    String WECHAT_MSG_ADD_STORY_AFTER = "主题\'s%\'创建成功";
    String WECHAT_MSG_ADD_STORY_TEXT_LENGTH = "超过长度,请输入10个中文以内";

    String WECHAT_MSG_UNDO_SUCCESS = "撤销成功";
    String WECHAT_MSG_UNDO_STORY_SUCCESS = "撤销成功,主题\'%s\'已删除";
    String WECHAT_MSG_UNDO_FAILE = "没有能撤销的数据";

    String WECHAT_MSG_NODE_IMAGE_SUCCESS = "图片保存成功";
    String WECHAT_MSG_NODE_AUDIO_SUCCESS = "语音保存成功";
    String WECHAT_MSG_NODE_VIDEO_SUCCESS = "视频保存成功";
    String WECHAT_MSG_NODE_LOCATION_SUCCESS = "地理位置设置成功";


    //微信用户cookie常量
    String WECHAT_OPEN_ID = "WECHAT_OPEN_ID";
    String WECHAT_ACCESSTOKEN = "WECHAT_ACCESSTOKEN";
    String WECHAT_REFRESHTOKEN = "WECHAT_REFRESHTOKEN";
    String WECHAT_UNION_ID = "WECHAT_UNION_ID";
    String WECHAT_STORY_ID = "WECHAT_STORY_ID";

}
