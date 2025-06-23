package com.point.chat.pointadmin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.point.chat.pointadmin.service.ChatFriendApplyService;
import com.point.chat.pointcommon.entity.ResultBean;
import com.point.chat.pointcommon.request.ChatFriendApplyAddRequest;
import com.point.chat.pointcommon.request.ChatFriendApplyAgreeRequest;
import com.point.chat.pointcommon.request.ChatFriendApplyReplayRequest;
import com.point.chat.pointcommon.response.ChatFriendApplyInfoResponse;
import com.point.chat.pointcommon.response.ChatFriendApplyResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天好友申请管理
 */
@Validated
@RestController
@RequestMapping("friend/apply")
@Tag(name = "聊天好友申请管理")
public class ChatFriendApplyController {

    @Resource
    private ChatFriendApplyService chatFriendApplyService;


    /**
     * 获取当前聊天用户
     */
    @Operation(summary = "获取好友申请列表")
    @GetMapping(value = "/list")
    public ResultBean<List<ChatFriendApplyResponse>> getChatFriendApplyList() {
        return ResultBean.success(chatFriendApplyService.getChatFriendApplyList());
    }

    /**
     * 获取当前聊天用户
     */
    @Operation(summary = "获取好友申请详情")
    @GetMapping(value = "/info")
    @Parameter(name = "applyId", description = "申请ID", required = true)
    public ResultBean<ChatFriendApplyInfoResponse> getChatFriendApplyInfo(@RequestParam("applyId") @NotNull(message = "申请ID不能为空") Integer applyId) {
        return ResultBean.success(chatFriendApplyService.getChatFriendApplyInfo(applyId));
    }

    /**
     * 添加好友申请
     */
    @Operation(summary = "添加好友申请")
    @PostMapping(value = "/add")
    public ResultBean<Boolean> addChatFriendApply(@RequestBody @Validated ChatFriendApplyAddRequest request) {
        return ResultBean.success(chatFriendApplyService.addChatFriendApply(request));
    }

    /**
     * 拒绝好友申请
     */
    @Operation(summary = "拒绝好友申请")
    @PostMapping(value = "/reject")
    public ResultBean<Boolean> rejectChatFriendApply(@RequestBody @Validated ChatFriendApplyAgreeRequest request){
        return ResultBean.success(chatFriendApplyService.rejectFriendApply(request));
    }


    /**
     * 回复好友申请
     */
    @Operation(summary = "回复好友申请")
    @PostMapping(value = "/replay")
    public ResultBean<Boolean> replayFriendApply(@RequestBody @Validated ChatFriendApplyReplayRequest request) {
        return ResultBean.success(chatFriendApplyService.replayFriendApply(request));
    }

    /**
     * 同意好友申请
     */
    @Operation(summary = "同意好友申请")
    @PostMapping(value = "/agree")
    public ResultBean<String> agreeFriendApply(@RequestBody @Validated ChatFriendApplyAgreeRequest request) {
        return ResultBean.success(chatFriendApplyService.agreeFriendApply(request),"同意");
    }


    @Operation(summary = "删除好友申请")
    @PostMapping(value = "/delete")
    @Parameter(name = "applyId", description = "申请ID", required = true)
    public ResultBean<Boolean> deleteFriendApply(@RequestParam("applyId") @NotNull(message = "申请ID不能为空") Integer applyId) {
        return ResultBean.success(chatFriendApplyService.deleteFriendApply(applyId));
    }

}
