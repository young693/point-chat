package com.point.chat.pointadmin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.point.chat.pointadmin.service.DSChatService;
import com.point.chat.pointcommon.entity.ResultBean;
import com.point.chat.pointcommon.request.DSChatRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("deepseek/chat")
@Tag(name = "DeepSeekAI管理")
public class DeepSeekAIController {

    @Resource
    private DSChatService dsChatService;

    /**
     * AI对话回复
     */
    @ApiOperationSupport(author = "daoyang@dot.cn")
    @Operation(summary = "AI对话回复")
        @PostMapping(value = "/completions")
    public ResultBean<String> generateChatMessage(@RequestBody @Validated DSChatRequest request) {
        return ResultBean.success(dsChatService.generateChatMessage(request), "完成");
    }


    /**
     * AI对话回复
     */
    @ApiOperationSupport(author = "daoyang@dot.cn")
    @Operation(summary = "AI对话回复(以流式返回响应)")
    @PostMapping(value = "/completions/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter generateChatMessageForStream(@RequestBody @Validated DSChatRequest request) {
        return dsChatService.generateChatMessageForStream(request);
    }

    /**
     * AI对话回复
     */
    @ApiOperationSupport(author = "daoyang@dot.cn")
    @Operation(summary = "关闭Sse请求")
    @PostMapping(value = "/closeSse")
    public ResultBean<Boolean> closeSse() {
        return ResultBean.result(dsChatService.closeSse());
    }
}
