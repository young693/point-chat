package com.point.chat.pointcommon.utils;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sse工具
 */
public class SseEmitterUtil {

    private static final Map<Integer, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();
    private static final Map<Integer, String> contentMap = new ConcurrentHashMap<>();

    public static boolean isExist(Integer userId) {
        return sseEmitterMap.containsKey(userId);
    }

    public static SseEmitter get(Integer userId) {
        return sseEmitterMap.get(userId);
    }

    public static SseEmitter add(Integer userId, SseEmitter sseEmitter) {
        sseEmitterMap.put(userId, sseEmitter);
        return sseEmitter;
    }

    public static void remove(Integer userId) {
        sseEmitterMap.remove(userId);
        contentMap.remove(userId);
    }

    public static String getContent(Integer userId) {
        return contentMap.get(userId);
    }

    public static void addContent(Integer userId, String content) {
        contentMap.put(userId, content);
    }

}
