package com.point.chat.pointadmin.util;

import com.alibaba.fastjson.JSONObject;

import com.point.chat.pointcommon.constants.DSConstant;
import com.point.chat.pointcommon.em.ExceptionCodeEm;
import com.point.chat.pointcommon.entity.DSChatRequestBody;
import com.point.chat.pointcommon.entity.DSChatRequestMessage;
import com.point.chat.pointcommon.exception.ApiException;
import com.point.chat.pointcommon.utils.HttpClientUtil;
import com.point.chat.pointcommon.utils.vo.HttpResponseVo;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * DeepSeek AI 工具类
 */
@Slf4j
@Component
public class DSUtils {

    @Value("${DEEPSEEK_APIKEY}")
    private String apiKey;
    private static String APIKEY;
    @PostConstruct
    public void init() {
        APIKEY = apiKey;
    }

    public static String generateChatMessage(List<DSChatRequestMessage> messages) {
        String url = getDSChatUrl();
        Map<String, String> header = HttpClientUtil.defaultHeader();
        header.put("Authorization", "Bearer " + getAPIKey());
        DSChatRequestBody requestBody = new DSChatRequestBody();
        requestBody.setMessages(messages);
        requestBody.setMax_tokens(128);
        HttpResponseVo responseVo = HttpClientUtil.doPost(url, header, JSONObject.toJSONString(requestBody));
        log.info("deepseek-chat-response:{}", responseVo.getHttpBody());
        if (responseVo.getStatus() == 200 && StringUtils.isNotBlank(responseVo.getHttpBody())) {
            JSONObject jsonObject = JSONObject.parseObject(responseVo.getHttpBody());
            return jsonObject.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        }
        return "服务器繁忙,请稍后重试";
    }

    public static void generateChatMessageForStream(List<DSChatRequestMessage> messages, Integer userId) {
        String url = getDSChatUrl();
        Map<String, String> header = HttpClientUtil.defaultHeader();
        header.put("Authorization", "Bearer " + getAPIKey());
        DSChatRequestBody requestBody = new DSChatRequestBody();
        requestBody.setMessages(messages);
        requestBody.setMax_tokens(1024);
        requestBody.setStream(true);
        HttpClientUtil.doPostForStream(url, header, JSONObject.toJSONString(requestBody), userId);
    }

    public static String getDSChatUrl() {
        return DSConstant.BASE_URL + DSConstant.CHAT_COMPLETION;
    }

    private static String getAPIKey() {
//        String apiKey = System.getenv(DSConstant.DS_API_EVN_KEY);
        String apiKey = APIKEY;
        if (StringUtils.isBlank(apiKey)) {
            throw new ApiException(ExceptionCodeEm.SYSTEM_ERROR, "未配置环境变量：" + DSConstant.DS_API_EVN_KEY);
        }
        return apiKey;
    }
}
