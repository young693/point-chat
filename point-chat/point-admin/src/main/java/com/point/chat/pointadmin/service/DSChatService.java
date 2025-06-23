package com.point.chat.pointadmin.service;

import com.point.chat.pointcommon.request.DSChatRequest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * DeepSeek AI 服务接口
 */
public interface DSChatService {

    String generateChatMessage(DSChatRequest request);

    SseEmitter generateChatMessageForStream(DSChatRequest request);
    /**
     * 中断sse连接
     */
    boolean closeSse();
}
