package com.point.chat.pointadmin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.point.chat.pointadmin.service.ChatMsgService;
import com.point.chat.pointcommon.entity.ResultBean;
import com.point.chat.pointcommon.model.ChatMsg;
import com.point.chat.pointcommon.request.ChatMsgSearchRequest;
import com.point.chat.pointcommon.response.ChatMessageResponse;
import com.point.chat.pointcommon.response.ChatUserMsgResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 聊天记录管理
 *
 * @author: yang.
 * @date: Created in 2024/1/10 18:11
 */
@Validated
@RestController
@RequestMapping("chat/record")
@Tag(name = "聊天记录管理")
public class ChatMsgController {

    @Resource
    private ChatMsgService chatMsgService;

    @Operation(summary = "用户聊天记录", description = "获取当前登录用户的所有聊天信息")
    @GetMapping(value= "/listAll")
    public ResultBean<List<ChatMessageResponse>> listAll() {
        return ResultBean.success(chatMsgService.search());
    }

    @Operation(summary = "群聊聊天消息", description = "获取群聊的所有消息")
    @GetMapping(value = "/listGroupChat")
    public ResultBean<List<ChatUserMsgResponse>> listGroupChat(@RequestParam(name = "chatId", required = true) String chatId){
        return ResultBean.success(chatMsgService.getMsgByChatId(chatId));
    }


    @Operation(summary = "单个用户聊天消息", description = "获取针对用户或者群聊的所有消息")
    @PostMapping(value = "/listSingleChat")
    public ResultBean<List<ChatUserMsgResponse>> listByChatId(@RequestBody ChatMsgSearchRequest searchRequest) {
        return ResultBean.success(chatMsgService.getMsgByChatId(searchRequest.getChatId()));
    }

    /**
     * 获取当前聊天用户
     */
    @Operation(summary = "聊天记录列表", description = "获取当前登录用户的聊天室列表")
    @GetMapping(value = "/listChatRoom")
    public ResultBean<List<ChatUserMsgResponse>> getChatRoomUserList(@Validated @RequestBody ChatMsgSearchRequest request) {
        return ResultBean.success(chatMsgService.getUserMsgList(request));
    }

    /**
     * 获取最近通话聊天记录
     */
    @Operation(summary = "获取最近通话聊天记录", description = "首次进入时页面时调用")
    @GetMapping(value = "/getLastCallMsg")
    public ResultBean<ChatUserMsgResponse> getLastCallMsg() {
        return ResultBean.success(chatMsgService.getLastCallMsg());
    }


    /**
     * 转发消息
     */
    @Operation(summary = "转发消息")
    @PostMapping(value = "/relay")
    @Parameters({
            @Parameter(name = "msgIds", description = "消息ID集合", required = true),
            @Parameter(name = "chatIds", description = "聊天室ID集合,与转发用户ID字段二选一"),
            @Parameter(name = "toUserIds", description = "转发用户ID集合,与聊天室ID字段二选一")
    })
    public ResultBean<Boolean> relayMsg(@RequestParam("msgIds") @NotEmpty(message = "消息ID集合不能为空") List<Integer> msgIds,
                                        @RequestParam(name = "chatIds", required = false) List<String> chatIds,
                                        @RequestParam(name = "toUserIds", required = false) List<Integer> toUserIds) {
        return ResultBean.result(chatMsgService.relayMsg(msgIds, chatIds, toUserIds));
    }

    /**
     * 删除消息
     */
    @Operation(summary = "删除消息")
    @PostMapping(value = "/delete")
    @Parameter(name = "msgId", description = "消息ID", required = true)
    public ResultBean<Boolean> deleteUserMsg(@RequestParam("msgId") @NotNull(message = "消息ID不能为空") Integer msgId) {
        return ResultBean.result(chatMsgService.deleteUserMsg(msgId));
    }

    /**
    * 删除所有消息
    */

     @Operation(summary="清空消息")
     @PostMapping(value="/cleanMsg")
     @Parameters({
            @Parameter(name="userId", description="用户ID集合", required = true),
            @Parameter(name="chatId", description="会话ID", required = true)
     })
     public ResultBean<Boolean> cleanMsg(@RequestParam("userId") Integer userId, @RequestParam("chatId") String chatId) {
         return ResultBean.result(chatMsgService.cleanMsgList(userId, chatId));
     }


    /**
     * 撤回消息
     */
    @Operation(summary = "撤回消息")
    @PostMapping(value = "/revoke")
    @Parameter(name = "msgId", description = "消息ID", required = true)
    public ResultBean<Boolean> revokeMsg(@RequestParam("msgId") @NotNull(message = "消息ID不能为空") Integer msgId) {
        return ResultBean.result(chatMsgService.revokeMsg(msgId));
    }

    /**
     * 更新文件消息上传状态
     */
    @Operation(summary = "更新文件消息上传状态")
    @PostMapping(value = "/updateFileMsgStatus")
    @Parameter(name = "msgId", description = "消息ID", required = true)
    public ResultBean<Boolean> updateFileMsgStatus(@RequestParam("msgId") @NotNull(message = "消息ID不能为空") Integer msgId) {
        return ResultBean.result(chatMsgService.updateFileMsgStatus(msgId));
    }
}
