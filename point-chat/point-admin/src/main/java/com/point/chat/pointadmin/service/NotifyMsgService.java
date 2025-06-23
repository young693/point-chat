package com.point.chat.pointadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.point.chat.pointcommon.em.EventTypeEm;
import com.point.chat.pointcommon.em.MsgTypeEm;
import com.point.chat.pointcommon.dto.NotifyMsgDto;
import com.point.chat.pointcommon.dto.NotifyMsgInfoDto;
import com.point.chat.pointcommon.model.NotifyMsg;
import com.point.chat.pointcommon.request.EntNotifyMsgSendRequest;
import com.point.chat.pointcommon.request.NotifyMsgSendRequest;
import com.point.chat.pointcommon.response.EntNotifyMsgResponse;
import com.point.chat.pointcommon.response.NotifyMsgResponse;


import java.util.List;

/**
 * 通知消息服务接口
 */
public interface NotifyMsgService extends IService<NotifyMsg> {

    /**
     * 获取通知消息列表
     *
     * @param linkid 关联ID(好友申请关联申请ID/业务ID)
     * @param userId 用户ID
     * @return 列表
     */
    List<NotifyMsgResponse> getFriendApplyNotifyMsgList(String linkid, Integer userId, String nickname);

    /**
     * 获取通知消息列表
     *
     * @param linkid 关联ID(好友申请关联申请ID/业务ID)
     * @param userId 用户ID
     * @return 列表
     */
    List<NotifyMsgDto> getFriendApplyNotifyMsgList(String linkid, Integer userId);

    /**
     * 获取通知消息通知信息的列表
     *
     * @return 列表
     */
    List<EntNotifyMsgResponse> getNotifyMsgNotifyList(Integer limit);

    /**
     * 获取通知消息列表
     *
     * @param linkid    关联ID(好友申请关联申请ID/业务ID)
     * @param userId    用户ID
     * @param msgType   消息类型(SYSTEM:系统消息;EVENT:事件消息;WARNING:预警消息)
     * @param eventType 事件类型(消息类型为事件消息时有值)
     * @return 列表
     */
    List<NotifyMsgDto> getNotifyMsgList(String linkid, Integer userId, MsgTypeEm msgType, EventTypeEm eventType, Integer limit);

    /**
     * 发送企业通知消息
     *
     * @param sendRequest 发送请求参数
     * @return true:发送成功;false:发送失败
     */
    boolean sendEntNotifyMsg(EntNotifyMsgSendRequest sendRequest);

    /**
     * 发送通知消息
     *
     * @param request 通知消息添加请求对象
     * @return true:发送成功;false:发送失败
     */
    boolean sendNotifyMsg(NotifyMsgSendRequest request);

    /**
     * 添加通知消息
     *
     * @param request 通知消息添加请求对象
     * @return true:发送成功;false:发送失败
     */
    boolean addNotifyMsg(NotifyMsgSendRequest request);

    /**
     * 更新消息已读状态
     *
     * @param linkid 关联ID(好友申请关联申请ID/业务ID)
     * @param userId 用户ID
     * @return boolean
     */
    boolean updateIsRead(String linkid, Integer userId);

    /**
     * 更新消息已读状态
     *
     * @param id       关联ID
     * @return boolean
     */
    boolean updateIsRead(Integer id);

    /**
     * 更新消息已读状态
     *
     * @return boolean
     */
    boolean allRead();

    /**
     * 更新消息已读状态
     *
     * @param id     关联ID
     * @param userId 用户ID
     * @return boolean
     */
    boolean updateIsRead(Integer userId, Integer id);

    /**
     * 获取离线通知消息列表
     *
     * @param userId 用户ID
     * @return 列表
     */
    List<NotifyMsgInfoDto> getOfflineNotifyMsgList(Integer userId);

    /**
     * 更新离线消息状态
     *
     * @param userId 用户ID
     * @return boolean
     */
    boolean updateOfflineNotifyMsg(Integer userId);


}
