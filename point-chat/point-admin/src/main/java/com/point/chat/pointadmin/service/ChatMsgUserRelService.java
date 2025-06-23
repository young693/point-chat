package com.point.chat.pointadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.point.chat.pointcommon.model.ChatMsgUserRel;

/**
 * 聊天信息和用户关联表服务接口
 */
public interface ChatMsgUserRelService extends IService<ChatMsgUserRel> {

    /**
     * 清空消息列表
     *
     * @param userId 用户id
     * @param chatId 聊天id
     * @return 是否成功
     */
    boolean cleanMsgList(Integer userId, String chatId);

    ChatMsgUserRel getChatMsgUserRel(Integer userId, Integer msgId);

    boolean deleteUserMsg(Integer userId, Integer msgId);

    /**
     * 根据消息ID清空用户消息列表
     *
     * @param msgId 消息ID
     * @return bool
     */
    boolean clearUserMsgByMsgId(Integer msgId);

    ChatMsgUserRel getLastMsg(Integer userId);
}
