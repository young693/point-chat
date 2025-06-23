package com.point.chat.pointadmin.listener;


import com.point.chat.pointadmin.listener.event.ChatMsgSendEvent;
import com.point.chat.pointadmin.tio.service.ChatMsgSendService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;


/**
 * 群聊事件监听
 */
@Slf4j
@Component
public class ChatMsgSendListener {

    @Resource
    private ChatMsgSendService chatMsgSendService;

    @TransactionalEventListener(ChatMsgSendEvent.class)
    public void onMsgSend(ChatMsgSendEvent event) {
        log.info("发送 {} 消息", event.getType());
        event.getMessageList().forEach(message -> {
            chatMsgSendService.sendAndSaveMsg(event.getChannelContext(), message);
        });
    }
}
