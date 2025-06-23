package com.point.chat.pointadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.point.chat.pointcommon.model.ChatFriendApply;
import com.point.chat.pointcommon.request.ChatFriendApplyAddRequest;
import com.point.chat.pointcommon.request.ChatFriendApplyAgreeRequest;
import com.point.chat.pointcommon.request.ChatFriendApplyReplayRequest;
import com.point.chat.pointcommon.response.ChatFriendApplyInfoResponse;
import com.point.chat.pointcommon.response.ChatFriendApplyResponse;
import com.point.chat.pointcommon.response.ChatUserResponse;

import java.util.List;

/**
 * 聊天室新好友申请表服务接口
 */
public interface ChatFriendApplyService extends IService<ChatFriendApply> {

    /**
     * 获取当前登录用户的好友申请列表
     *
     * @return 好友申请列表
     */
    List<ChatFriendApplyResponse> getChatFriendApplyList();

    /**
     * 获取好友申请详情
     *
     * @param applyId  申请ID
     * @return 申请详情
     */
    ChatFriendApplyInfoResponse getChatFriendApplyInfo(Integer applyId);

    /**
     * 添加好友申请
     *
     * @param request 添加好友申请请求
     * @return true 成功，false 失败
     */
    boolean addChatFriendApply(ChatFriendApplyAddRequest request);

    /**
     * 添加好友申请, 返回申请ID
     *
     * @param request 添加好友申请请求
     * @return 申请ID
     */
    Integer addChatFriendApplyRetApplyId(ChatFriendApplyAddRequest request);

    boolean rejectFriendApply(ChatFriendApplyAgreeRequest request);

    /**
     * 回复好友申请
     *
     * @param request 请求参数
     * @return true 成功，false 失败
     */
    boolean replayFriendApply(ChatFriendApplyReplayRequest request);

    /**
     * 同意好友申请
     *
     * @return chatId
     */
    String agreeFriendApply(ChatFriendApplyAgreeRequest request);

    /**
     * 同意好友申请
     *
     * @return chatId
     */
    String agreeFriendApplyRetChatId(ChatFriendApplyAgreeRequest request, ChatUserResponse chatUser);

    /**
     * 删除好友申请
     *
     * @param applyId 申请ID
     * @return true 成功，false 失败
     */
    boolean deleteFriendApply(Integer applyId);

    /**
     * 清空未读数
     *
     * @param applyId 申请ID
     * @return true 成功，false 失败
     */
    boolean clearUnreadCount(Integer applyId);

    /**
     * 清空未读数
     *
     * @param applyId 申请ID
     * @param userId 用户ID
     * @return true 成功，false 失败
     */
    boolean clearUnreadCount(Integer applyId, Integer userId);
}
