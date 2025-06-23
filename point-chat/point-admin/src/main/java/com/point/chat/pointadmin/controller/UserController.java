package com.point.chat.pointadmin.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.point.chat.pointadmin.service.ChatUserService;
import com.point.chat.pointadmin.service.UserService;
import com.point.chat.pointcommon.entity.ResultBean;
import com.point.chat.pointcommon.manager.TokenManager;
import com.point.chat.pointcommon.request.LoginRequest;
import com.point.chat.pointcommon.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户注册登录界面
 */
@Validated
@RestController
@RequestMapping("user/regulate")
@Tag(name = "用户管理")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private ChatUserService chatUserService;

    @Operation(summary = "获取登录用户信息")
    @GetMapping(value = "/info")
    public ResultBean getUser() {
        return ResultBean.success(chatUserService.getCurrentChatUser());
    }

    @Operation(summary = "获取指定用户信息")
    @GetMapping(value = "/userInfo")
    public ResultBean getUserById(@RequestParam("userId") @NotNull(message = "userId不能为空") Integer userId){
        return ResultBean.success(chatUserService.getChatUser(userId));
    }


    /**
     * 账号密码登录
     */
    @Operation(summary = "账号密码登录")
    @PostMapping(value = "/login")
    public ResultBean<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest) {
        return ResultBean.success(userService.login(loginRequest));
    }

    /**
     * 刷新token
     */
    @Operation(summary = "刷新token")
    @PostMapping(value = "/refreshToken")
    public ResultBean<LoginResponse> refreshToken() {
        return ResultBean.success(userService.refreshToken());
    }

    /**
     * 退出登录
     */
    @Operation(summary = "退出登录")
    @PostMapping(value = "/logout")
    public ResultBean<Boolean> login() {
        return ResultBean.success();
    }

    @Operation(summary = "更新密码")
    @PostMapping("/updatePassword")
    @Parameters({
            @Parameter(name = "oldPwd", description = "原密码", required = true),
            @Parameter(name = "newPwd", description = "新密码(长度为6-20)", required = true)
    })
    public ResultBean<Boolean> updatePassword(
            @RequestParam("oldPwd") @NotBlank(message = "原密码不能为空") String oldPwd,
            @RequestParam("newPwd") @NotBlank(message = "新密码不能为空") @Length(min = 6, max = 20, message = "新密码长度为6-20位") String newPwd) {
        return ResultBean.result(userService.updatePassword(oldPwd, newPwd));
    }

    @Operation(summary = "注册并登录")
    @PostMapping("/registerAndLogin")
    @Parameters({
            @Parameter(name = "account", description = "手机号(长度11位)", required = true),
            @Parameter(name = "password", description = "密码(长度为6-20)", required = true),
            @Parameter(name = "nickname", description = "昵称(最大长度32位)")
    })
    public ResultBean<LoginResponse> register(
            @RequestParam("account") @NotBlank(message = "手机号不能为空") @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式错误") String account,
            @RequestParam("password") @NotBlank(message = "密码不能为空") @Length(min = 6, max = 20, message = "密码长度为6-20位") String password,
            @RequestParam(name = "nickname", required = false) @Length(max = 32, message = "昵称长度不能超过32位") String nickname) {

        return ResultBean.success(userService.registerAndLogin(account, password, nickname));
    }
}

