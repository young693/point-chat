package com.point.chat.pointadmin.controller;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.point.chat.pointadmin.service.ChatUserService;
import com.point.chat.pointcommon.entity.ResultBean;
import com.point.chat.pointcommon.response.ChatUserInfoResponse;
import com.point.chat.pointcommon.response.ChatUserResponse;
import com.point.chat.pointcommon.response.ChatUserSearchResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 聊天用户管理
 *
 * @author: Dao-yang.
 * @date: Created in 2024/1/10 18:11
 */
@Validated
@RestController
@RequestMapping("chat/user")
@Tag(name = "聊天用户管理")
public class ChatUserController {

    @Resource
    private ChatUserService chatUserService;

    /**
     * 获取当前聊天用户
     */
    @Operation(summary = "获取当前聊天用户")
    @GetMapping(value = "/get")
    public ResultBean<ChatUserResponse> getCurrentChatUser() {
        return ResultBean.success(chatUserService.getCurrentChatUser());
    }

    /**
     * 搜索聊天用户，包含好友
     */
    @Operation(summary = "搜索聊天用户", description = "搜索好友，包含好友")
    @GetMapping(value = "/getFriendList")
    @Parameter(name = "keyword", description = "关键词", required = true)
    public ResultBean<List<ChatUserSearchResponse>> getSearchChatUserList(
            @RequestParam("keyword") @NotNull(message = "关键词不能为空") String keyword) {
        return ResultBean.success(chatUserService.getSearchFriendList(keyword));
    }

    /**
     * 搜索聊天用户 搜索添加好友时使用,不包含好友
     */
    @Operation(summary = "搜索用户", description = "搜索添加好友时使用,不包含好友")
    @GetMapping(value = "/getSearchList")
    @Parameter(name = "keyword", description = "关键词", required = true)
    public ResultBean<List<ChatUserSearchResponse>> getSearchnotChatUserList(
            @RequestParam("keyword") @NotNull(message = "关键词不能为空") String keyword) {
        return ResultBean.success(chatUserService.getSearchChatUserList(keyword));
    }

    /**
     * 获取用户详情
     */
    @Operation(summary = "获取用户详情")
    @GetMapping(value = "/info")
    @Parameter(name = "userId", description = "用户ID", required = true)
    public ResultBean<ChatUserInfoResponse> getChatUserFriendInfo(
            @RequestParam("userId") @NotNull(message = "用户ID不能为空") Integer userId) {
        return ResultBean.success(chatUserService.getChatUserInfo(userId));
    }

    @Operation(summary = "更新用户头像")
    @PostMapping("/updateAvatar")
    @Parameter(name = "image", description = "图片文件", in = ParameterIn.DEFAULT, required = true,
            schema = @Schema(name = "image", format = "binary"))
    public ResultBean<String> updateAvatar(
            @RequestParam("image") @NotNull(message = "上传文件不能为空") MultipartFile image) {
        return ResultBean.success(chatUserService.updateAvatar(image), "操作成功");

    }

    @Operation(summary = "更新用户头像")
    @PostMapping("/updateAvatarByUrl")
    @Parameter(name = "url", description = "图片路径")
    public ResultBean<Boolean> updateAvatarByUrl(@RequestParam("url") @NotNull(message = "路径不能为空") String url) {
        return ResultBean.success(chatUserService.updateAvatarByUrl(url));
    }


    @Operation(summary = "更新用户信息")
    @PostMapping("/updateNickname")
    @Parameters({
            @Parameter(name = "nickname", description = "用户昵称"),
            @Parameter(name = "sex", description = "性别(0:保密,1:男,2:女)"),
            @Parameter(name = "signature", description = "个性签名")
    })
    public ResultBean<Boolean> updateNickname(@RequestParam(name = "nickname", required = false) String nickname,
                                              @RequestParam(name = "sex", required = false) Integer sex,
                                              @RequestParam(name = "signature", required = false) String signature) {

        return ResultBean.result(chatUserService.updateNickname(nickname, sex, signature));
    }

}
