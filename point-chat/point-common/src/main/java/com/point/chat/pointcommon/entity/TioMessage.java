package com.point.chat.pointcommon.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;

import com.point.chat.pointcommon.em.ChatTypeEm;

import com.point.chat.pointcommon.em.EventTypeEm;
import com.point.chat.pointcommon.em.MsgTypeEm;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * Socket 发送消息模板
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TioMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Integer id;

    /**
     * 撤回消息的ID
     */
    private Integer revokeId;


    /**
     * 聊天室ID
     */
    private String chatId;

    /**
     * 消息类型(TEXT:文本消息;PRODUCT:商品消息;IMAGE:图片消息;VIDEO:视频消息;FILE:文件消息;BIZ_CARD:个人名片消息;GROUP_BIZ_CARD:群名片消息;
     * SYSTEM:系统消息;EVENT:事件消息;WARNING:预警消息;HEART_BEAT:心跳监测消息;NOTICE:通知消息;)
     */
    private MsgTypeEm msgType;

    /**
     * 事件类型(消息类型为事件消息时有值)
     */
    private EventTypeEm eventType;

    /**
     * 响应code
     */
    private int code = 200;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 聊天类型(SINGLE:单聊,GROUP:群聊)
     */
    private ChatTypeEm chatType;

    /**
     * 发送用户ID
     */
    private Integer sendUserId;

    /**
     * 接收人ID/群组ID
     */
    private Integer toUserId;

    /**
     * 发送时间
     */
    private String sendTime;

    /**
     * 设备类型(PC,MOBILE)
     */
    private String deviceType;

    public TioMessage() {
    }

    public TioMessage(MsgTypeEm msgType, String msg) {
        this.msgType = msgType;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
