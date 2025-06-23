package com.point.chat.pointadmin.service;

import com.point.chat.pointcommon.request.LoginRequest;
import com.point.chat.pointcommon.response.LoginResponse;


public interface UserService {

    /**
     * 账号密码登录
     *
     * @return LoginResponse
     */
    LoginResponse login(LoginRequest loginRequest);

    LoginResponse refreshToken();

    void logout();

    /**
     * 更新密码
     *
     * @param oldPwd   旧密码
     * @param newPwd   新密码
     * @return 更新结果
     */
    Boolean updatePassword(String oldPwd, String newPwd);

    /**
     * 注册并登录
     *
     * @param phone    手机号
     * @param password 密码
     * @return 注册结果
     */
    LoginResponse registerAndLogin(String phone, String password, String nickname);
}
