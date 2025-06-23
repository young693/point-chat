package com.point.chat.pointadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.point.chat.pointcommon.model.ChatRoomUserRel;

import java.util.List;

/**
 * 聊天室和用户关联表服务接口
 */
public interface ChatRoomUserRelService extends IService<ChatRoomUserRel> {

    /**
     * 获取聊天室用户关系列表
     *
     * @param chatId 聊天室ID
     * @return 聊天室用户关系列表
     */
    List<ChatRoomUserRel> getChatRoomUserRelListByChatId(String chatId);

    /**
     * 获取聊天室用户关系
     *
     * @param chatId 聊天室ID
     * @param userId 用户ID
     * @return 聊天室关系
     */
    ChatRoomUserRel getChatRoomUserRel(String chatId, Integer userId);
}
