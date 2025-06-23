package com.point.chat.pointadmin.listener.event;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 群公告更新事件
 */

@Data
public class ChatGroupNoticeUpdateEvent implements Serializable {

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
     * 群成员昵称
     */
    private String nickname;


    public ChatGroupNoticeUpdateEvent(Integer groupId, Integer userId, String nickname) {
        this.groupId = groupId;
        this.userId = userId;
        this.nickname = nickname;
    }
}
