package com.point.chat.pointadmin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.point.chat.pointadmin.service.ChatRoomService;
import com.point.chat.pointcommon.entity.ResultBean;
import com.point.chat.pointcommon.response.ChatRoomMsgInfoResponse;
import com.point.chat.pointcommon.response.ChatRoomUserResponse;
import com.point.chat.pointcommon.response.ChatUnreadCountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天室管理
 */
@Validated
@RestController
@RequestMapping("chat/room")
@Tag(name = "聊天室管理")
public class ChatRoomController {

    @Resource
    private ChatRoomService chatRoomService;

    /**
     * 获取当前聊天用户
     */
    @Operation(summary = "聊天室列表", description = "获取当前登录用户的聊天室列表")
    @GetMapping(value = "/list")
    public ResultBean<List<ChatRoomUserResponse>> getChatRoomUserList() {
        return ResultBean.success(chatRoomService.getChatRoomUserList());
    }

    /**
     * 刷新聊天室列表
     */
    @Operation(summary = "刷新聊天室列表", description = "获取当前登录用户的聊天室列表")
    @GetMapping(value = "/refresh/list")
    public ResultBean<List<ChatRoomUserResponse>> getChatRoomUserRefreshList() {
        return ResultBean.success(chatRoomService.getChatRoomUserRefreshList());
    }

    /**
     * 获取聊天室未读数
     */
    @Operation(summary = "获取聊天室未读数", description = "获取聊天室消息和好友申请未读数")
    @GetMapping(value = "/getUnreadCount")
    public ResultBean<ChatUnreadCountResponse> getChatUnreadCount() {
        return ResultBean.success(chatRoomService.getChatUnreadCount());
    }

    /**
     * 从好友列表去聊天室
     */
    @Operation(summary = "去聊天室", description = "从好友列表去聊天室")
    @GetMapping(value = "/gotoSendMsg")
    @Parameter(name = "friendId", description = "好友ID", required = true)
    public ResultBean<String> gotoSendMsg(@RequestParam("friendId") @NotNull(message = "好友ID不能为空") Integer friendId) {
        return ResultBean.success(chatRoomService.gotoSendMsg(friendId), "成功");
    }

    /**
     * 获取聊天室信息详情
     */
    @Operation(summary = "获取聊天室信息详情")
    @GetMapping(value = "/getChatRoomMsgInfo")
    @Parameter(name = "chatId", description = "聊天室ID", required = true)
    public ResultBean<ChatRoomMsgInfoResponse> getChatRoomMsgInfo(@RequestParam("chatId") @NotBlank(message = "聊天室ID不能为空") String chatId) {
        return ResultBean.success(chatRoomService.getChatRoomMsgInfo(chatId));
    }

    /**
     * 更新消息免打扰
     */
    @Operation(summary = "更新消息免打扰")
    @PostMapping(value = "/updateMsgNoDisturb")
    @Parameters({
            @Parameter(name = "chatId", description = "聊天室ID", required = true),
            @Parameter(name = "flag", description = "消息免打扰", required = true)
    })
    public ResultBean<Boolean> updateMsgNoDisturb(@RequestParam("chatId") @NotBlank(message = "聊天室ID不能为空") String chatId,
                                                  @RequestParam("flag") @NotNull(message = "消息免打扰不能为空") Boolean flag) {
        return ResultBean.success(chatRoomService.updateMsgNoDisturb(chatId, flag));
    }

    /**
     * 是否置顶
     */
    @Operation(summary = "是否置顶")
    @PostMapping(value = "/updateIsTop")
    @Parameters({
            @Parameter(name = "chatId", description = "聊天室ID", required = true),
            @Parameter(name = "flag", description = "是否置顶", required = true)
    })
    public ResultBean<Boolean> updateIsTop(@RequestParam("chatId") @NotBlank(message = "聊天室ID不能为空") String chatId,
                                           @RequestParam("flag") @NotNull(message = "是否置顶不能为空") Boolean flag) {
        return ResultBean.success(chatRoomService.updateIsTop(chatId, flag));
    }

    /**
     * 清空聊天记录
     */
    @Operation(summary = "清空聊天记录")
    @PostMapping(value = "/cleanMsgList")
    @Parameter(name = "chatId", description = "聊天室ID", required = true)
    public ResultBean<Boolean> cleanMsgList(@RequestParam("chatId") @NotBlank(message = "聊天室ID不能为空") String chatId) {
        return ResultBean.success(chatRoomService.cleanMsgList(chatId));
    }


    /**
     * 删除聊天室
     */
    @Operation(summary = "删除聊天室")
    @PostMapping(value = "/delete")
    @Parameter(name = "chatId", description = "聊天室ID", required = true)
    public ResultBean<Boolean> deleteChatRoom(@RequestParam("chatId") @NotBlank(message = "聊天室ID不能为空") String chatId) {
        return ResultBean.success(chatRoomService.deleteChatRoom(chatId));
    }
}
