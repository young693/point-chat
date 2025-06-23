package com.point.chat.pointadmin.listener.event;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 退出群聊事件
 */

@Data
public class ChatGroupLogoutEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 6238525473174356147L;

    /**
     * 群组ID
     */
    private Integer groupId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 昵称
     */
    private String nickname;


    public ChatGroupLogoutEvent(Integer groupId, Integer userId, String nickname) {
        this.groupId = groupId;
        this.userId = userId;
        this.nickname = nickname;
    }
}
