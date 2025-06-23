package com.point.chat.pointcommon.config;

import lombok.Data;

/**
 * 聊天室配置
 */
@Data
public class ChatConfig {

    /**
     * 聊天室图标,生成群二维码用
     */
    private String icon;

    /**
     * 群组配置
     */
    private ChatGroupConfig group;

    @Data
    public static class ChatGroupConfig {

        /**
         * 群组最大人数
         */
        private Integer maxMemberNum;

        /**
         * 群二维码链接
         */
        private String qrcodeUrl;
    }
}
