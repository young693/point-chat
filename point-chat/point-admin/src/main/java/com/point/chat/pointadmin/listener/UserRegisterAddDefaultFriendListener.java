package com.point.chat.pointadmin.listener;

import cn.hutool.core.util.ObjectUtil;
import com.point.chat.pointadmin.listener.event.UserRegisterAddDefaultFriendEvent;
import com.point.chat.pointadmin.service.ChatFriendApplyService;
import com.point.chat.pointcommon.em.ChatSourceEm;
import com.point.chat.pointcommon.em.ExceptionCodeEm;
import com.point.chat.pointcommon.exception.ApiException;
import com.point.chat.pointcommon.request.ChatFriendApplyAddRequest;
import com.point.chat.pointcommon.request.ChatFriendApplyAgreeRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 群聊事件监听
 */
@Slf4j
@Component
public class UserRegisterAddDefaultFriendListener {

    @Resource
    private ChatFriendApplyService chatFriendApplyService;

    @EventListener(UserRegisterAddDefaultFriendEvent.class)
    public void onUserRegisterAfter(UserRegisterAddDefaultFriendEvent event) {
        log.info("用户 {} 添加默认好友 {}", event.getUserId(), event.getFriendUser().getNickname());
        ChatFriendApplyAddRequest applyAddRequest = new ChatFriendApplyAddRequest();
        applyAddRequest.setSource(ChatSourceEm.SYSTEM.name());
        applyAddRequest.setChatUserId(event.getUserId());
        applyAddRequest.setApplyReason("我是" + event.getFriendUser().getNickname());
        applyAddRequest.setFriendId(event.getFriendUser().getId());
        applyAddRequest.setRemark("");
        Integer applyId = chatFriendApplyService.addChatFriendApplyRetApplyId(applyAddRequest);
        if (ObjectUtil.isNull(applyId)) {
            log.error("用户 {} 添加默认好友{}失败", event.getUserId(), event.getFriendUser().getNickname());
            throw new ApiException(ExceptionCodeEm.SYSTEM_ERROR, "添加好友失败");
        }

        ChatFriendApplyAgreeRequest agreeRequest = new ChatFriendApplyAgreeRequest();
        agreeRequest.setApplyId(applyId);
        agreeRequest.setNickname(event.getFriendUser().getNickname());
        String chatId = chatFriendApplyService.agreeFriendApplyRetChatId(agreeRequest, event.getFriendUser());
        if (ObjectUtil.isNull(chatId)) {
            log.error("用户 {} 添加默认好友{}失败,chatId:{}", event.getUserId(), event.getFriendUser().getNickname(), chatId);
            throw new ApiException(ExceptionCodeEm.SYSTEM_ERROR, "添加好友失败");
        }
    }
}
