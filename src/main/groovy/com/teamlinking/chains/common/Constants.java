package com.teamlinking.chains.common;

/**
 * Created by admin on 16/1/14.
 */
public interface Constants {

    enum WechatMenu {

        currentStory("CURRENT_STORY","当前状态"),
        updateStory("UPDATE_STORY","修改主题"),
        addStory("ADD_STORY","新增主题"),
        addSubStory("ADD_SUB_STORY","新增子主题"),
        nextStory("NEXT_STORY","下一个主题"),
        sonStory("SON_STORY","进入子主题"),
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

        public static WechatCommand pase(String key){
            if (key != null){
                for (WechatCommand type : WechatCommand.values()){
                    if (type.key.equals(key)){
                        return type;
                    }
                }
            }
            return null;
        }

    }

    enum NodeType{
        text("文字",1),
        pic("图片",8),
        video("视频",32),
        textAndPic("图文",9),
        textAndVideo("文字+视频",33);

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
            if ((this == textAndPic || this == textAndVideo)
                    && (nt == text || nt == pic || nt == video)){
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

    enum FileType {
        pic("图片", 8),
        video("视频", 32);

        public String key;
        public int value;

        FileType(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    enum OwnerType {
        story("主题", 1),
        node("节点", 2);

        public String key;
        public int value;

        OwnerType(String key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    //Base ENCODE
    String ENCODE_32_SALT = "8esflhw9y2435kcr6xqinojzm7pgd1tv";
    //时间设置格式
    String DATE_SET_EL = "^#\\d{4}.*#$";

    //微信文字命令
    String WECHAT_MSGTYPE_TEXT_COMMAND_UNDO = "取消";
    String WECHAT_MSGTYPE_TEXT_COMMAND_HELP = "帮助";

    String DEFAULT_STORY_BACK_GROUND = "http://77l5lr.com1.z0.glb.clouddn.com/bg.png";

    /** 微信消息返回 **/
    String WECHAT_MSG_SUBSCRIBE = "你好!欢迎来到记忆的空间,你可以回复'帮助'两字查看使用说明";
    String WECHAT_MSG_AUDIO_RETURN = "系统不支持语音消息,请发送文字、图片或视频";
    String WECHAT_MSG_CURRENT_STORY = "主题:\n%s\n当前操作:%s";
    String WECHAT_MSG_NEXT_STORY = "切换完成,当前主题是'%s'";
    String WECHAT_MSG_NEXT_STORY_FAILE = "没有主题可以切换,当前主题是'%s'";
    String WECHAT_MSG_BACK_PARENT = "返回上级成功,当前主题是'%s'";
    String WECHAT_MSG_BACK_PARENT_FAILE = "该主题已经是最上级,当前主题是'%s'";

    String WECHAT_MSG_UPDATE_STORY_BEFORE = "你正在修改主题'%s',回复文字将修改标题(10个中文以内),回复图片将修改主题配图";
    String WECHAT_MSG_UPDATE_STORY_IMAGE_AFTER = "主题'%s'配图修改成功";

    String WECHAT_MSG_ADD_STORY_BEFORE = "请输入标题10个中文以内";
    String WECHAT_MSG_ADD_SUB_STORY_BEFORE = "你正在主题'%s'中增加子主题,请输入标题10个中文以内";
    String WECHAT_MSG_ADD_STORY_IMAGE = "请上传主题'%s'配图.如你想删除该主题,回复文字'取消'或使用操作菜单中'撤销'按钮";
    String WECHAT_MSG_ADD_STORY_AFTER = "主题'%s'创建成功";
    String WECHAT_MSG_ADD_STORY_TEXT_LENGTH = "超过长度,请输入10个中文以内";

    String WECHAT_MSG_UNDO_SUCCESS = "撤销成功";
    String WECHAT_MSG_UNDO_STORY_SUCCESS = "撤销成功,主题'%s'已删除";
    String WECHAT_MSG_UNDO_FAILE = "没有能撤销的数据";

    String WECHAT_MSG_NODE_TEXT_SUCCESS = "保存成功";
    String WECHAT_MSG_NODE_IMAGE_SUCCESS = "图片保存成功";
    String WECHAT_MSG_NODE_VIDEO_SUCCESS = "视频保存成功";
    String WECHAT_MSG_NODE_LOCATION_SUCCESS = "地理位置设置成功";
    String WECHAT_MSG_NODE_FAILE = "你正在执行'%s',请先完成流程后再执行,或者回复文字'取消'或使用操作菜单中'撤销'按钮中断流程";

    String WECHAT_MSG_NODE_NO_DATA_FAILE = "没有可操作的数据";
    String WECHAT_MSG_NODE_DATE_FAILE = "时间格式错误,请按住下列格式设置:\n#2016-01-23#\n或\n#2016年1月23号#";
    String WECHAT_MSG_NODE_DATE_SUCCESS = "时间设置成功";

    String WECHAT_HELP_PRODUCT_description = "";
    String WECHAT_HELP_PRODUCT_picUrl = "";
    String WECHA_HELPT_PRODUCT_title = "";
    String WECHA_HELPT_PRODUCT_url = "";

    String WECHAT_HELP_TEST_description = "啦啦啦！\n" +
            "啦啦啦！\n" +
            "我是卖报的小行家，\n" +
            "不等天明去等派报";
    String WECHAT_HELP_TEST_picUrl = "https://mmbiz.qlogo.cn/mmbiz/1wnvtA5gUfEbdzibAwciaic2fpbibYxNwYHBLMurO1macwibXSORrgxMxjVibd1sh9nduplibiamObrZEicjPYORa80khXA/0?wx_fmt=png";
    String WECHA_HELPT_TEST_title = "帮助文档";
    String WECHA_HELPT_TEST_url = "http://mp.weixin.qq.com/s?__biz=MzAxODc3ODkwOQ==&mid=402285247&idx=1&sn=3bd3dd4b826e62c12997ea8f44819d43#rd";

    //微信用户cookie常量
    String WECHAT_OPEN_ID = "WECHAT_OPEN_ID";
    String WECHAT_ACCESSTOKEN = "WECHAT_ACCESSTOKEN";
    String WECHAT_REFRESHTOKEN = "WECHAT_REFRESHTOKEN";
    String WECHAT_UNION_ID = "WECHAT_UNION_ID";
    String WECHAT_STORY_ID = "WECHAT_STORY_ID";

    //七牛存储域名
    String QINIU_DOMAIN = "http://teamlinking.u.qiniudn.com/";

}
