package com.point.chat.pointadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.point.chat.pointcommon.dto.ChatUserMsgDto;
import com.point.chat.pointcommon.model.ChatMsg;
import com.point.chat.pointcommon.request.ChatMsgAddRequest;
import com.point.chat.pointcommon.request.ChatMsgSearchRequest;
import com.point.chat.pointcommon.response.ChatMessageResponse;
import com.point.chat.pointcommon.response.ChatUserMsgResponse;


import java.util.List;

/**
 * 会话消息表服务接口
 */
public interface ChatMsgService extends IService<ChatMsg> {

    /**
     * 根据消息ID获取消息(不包含消息内容字段)
     *
     * @param msgId 消息ID
     * @return 消息
     */
    ChatMsg get(Integer msgId);


    List<ChatUserMsgResponse> getMsgByChatId(String chatId);

    /**
     * 返回跟当前用户相关的会话，包括用户和群聊
     */
    List<ChatMessageResponse> search();


    /**
     * 获取离线消息
     *
     * @param userId 用户id
     * @return 离线消息列表
     */
    List<ChatUserMsgDto> getOfflineMsgList(Integer userId);

    /**
     * 获取用户消息列表
     *
     * @param request 搜索对象
     * @return 用户消息列表
     */
    List<ChatUserMsgResponse> getUserMsgList(ChatMsgSearchRequest request);

    /**
     * 获取最近一条通话消息
     *
     * @return 最近一条通话消息
     */
    ChatUserMsgResponse getLastCallMsg();

    boolean updateOfflineMsg(Integer userId, Integer msgId);

    boolean updateMsgContent(Integer msgId, String msgContent);

    boolean batchUpdateOfflineMsg(Integer userId, List<Integer> msgIds);

    Integer saveMsg(ChatMsgAddRequest request);

    /**
     * 清空消息列表
     *
     * @param userId 用户id
     * @param chatId 聊天id
     * @return 是否成功
     */
    boolean cleanMsgList(Integer userId, String chatId);

    /**
     * 消息转发
     *
     * @param msgIds    消息ID
     * @param chatIds   转发聊天室ID
     * @param toUserIds 转发用户ID
     * @return bool
     */
    boolean relayMsg(List<Integer> msgIds, List<String> chatIds, List<Integer> toUserIds);

    /**
     * 删除消息(只删除当前用户的消息记录)
     *
     * @param msgId    消息ID
     * @return bool
     */
    boolean deleteUserMsg(Integer msgId);

    /**
     * 撤回消息(删除所有用户的消息记录)
     *
     * @param msgId    消息ID
     * @return bool
     */
    boolean revokeMsg(Integer msgId);

    boolean updateFileMsgStatus(Integer msgId);

}
