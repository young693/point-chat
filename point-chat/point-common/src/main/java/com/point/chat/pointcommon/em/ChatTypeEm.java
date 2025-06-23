package com.point.chat.pointcommon.em;

/**
 * 聊天室类型枚举
 */
public enum ChatTypeEm {

    SINGLE("单聊"),
    GROUP("群聊");


    private String desc;

    ChatTypeEm(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static ChatTypeEm getChatType(String name) {
        for (ChatTypeEm typeEm : ChatTypeEm.values()) {
            if (typeEm.name().equals(name)) {
                return typeEm;
            }
        }
        return null;
    }

    public static String getChatTypeDesc(String type) {
        for (ChatTypeEm typeEm : ChatTypeEm.values()) {
            if (typeEm.getDesc().equals(type)) {
                return typeEm.getDesc();
            }
        }
        return null;
    }
}
