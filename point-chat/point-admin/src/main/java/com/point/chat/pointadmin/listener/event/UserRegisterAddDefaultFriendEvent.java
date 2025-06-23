package com.point.chat.pointadmin.listener.event;

import com.point.chat.pointcommon.response.ChatUserResponse;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户注册后添加默认好友事件
 */

@Data
public class UserRegisterAddDefaultFriendEvent implements Serializable {

    @Serial
    private static final long serialVersionUID = 6238525473174356147L;
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 好友
     */
    private ChatUserResponse friendUser;


    public UserRegisterAddDefaultFriendEvent(Integer userId) {
        friendUser = new ChatUserResponse();
        friendUser.setId(0);
        friendUser.setNickname("体验交互账号");
        this.userId = userId;
    }
}
