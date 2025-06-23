package com.point.chat.pointadmin.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.point.chat.pointadmin.service.ChatUserService;
import com.point.chat.pointadmin.service.UserService;
import com.point.chat.pointcommon.constants.CommConstant;
import com.point.chat.pointcommon.em.ExceptionCodeEm;
import com.point.chat.pointcommon.entity.LoginUsername;
import com.point.chat.pointcommon.entity.TokenModel;
import com.point.chat.pointcommon.exception.ApiException;
import com.point.chat.pointcommon.manager.TokenManager;
import com.point.chat.pointcommon.model.ChatUser;
import com.point.chat.pointcommon.request.LoginRequest;
import com.point.chat.pointcommon.response.LoginResponse;
import com.point.chat.pointcommon.utils.AESUtil;
import com.point.chat.pointcommon.utils.RedisUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private ChatUserService chatUserService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private TokenManager tokenManager;

    @Resource(name = "redisUtil")
    private RedisUtil redisUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        ChatUser user = chatUserService.getByPhone(loginRequest.getAccount());
        if (ObjectUtil.isNull(user)) {
            log.error("账号不存在,phone:{}", loginRequest.getAccount());
            throw new ApiException(ExceptionCodeEm.NOT_FOUND, "此账号未注册");
        }
        if (!user.getStatus()) {
            throw new ApiException(ExceptionCodeEm.UNAUTHORIZED, "此账号被禁用");
        }
//        String password = AESUtil.encryptCBC(loginRequest.getAccount(), loginRequest.getPassword());
        if(!passwordEncoder.matches(loginRequest.getPassword(), user.getPwd())){
            throw new ApiException(ExceptionCodeEm.VALIDATE_FAILED, "密码错误");
        }
        return getLoginResponseAndToken(user);
    }

    private LoginResponse getLoginResponseAndToken(ChatUser user) {
        // 设置登录token
        LoginResponse loginResponse = getLoginResponse(user);

        // 记录最后一次登录时间和IP
        chatUserService.updateLastLoginIpTime(user.getId());
        return loginResponse;
    }

    private LoginResponse getLoginResponse(ChatUser user) {
        LoginResponse loginResponse = new LoginResponse();
        LoginUsername loginUsername = new LoginUsername(user.getId(), user.getPhone(), user.getNickname());
        if(redisUtil.exists(loginUsername.mergeUsername())){
            String token = redisUtil.get(loginUsername.mergeUsername());
            redisUtil.remove(token);
            redisUtil.remove(loginUsername.mergeUsername());
        }
        TokenModel token = tokenManager.createToken(loginUsername);
        loginResponse.setToken(token.getToken());
        loginResponse.setExpiresIn(token.getExpiresIn());
        loginResponse.setLastAccessedTime(token.getLastAccessedTime());
        return loginResponse;
    }

    @Override
    public LoginResponse refreshToken() {
        TokenModel tokenModel = tokenManager.refreshToken();
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(tokenModel.getToken());
        loginResponse.setExpiresIn(tokenModel.getExpiresIn());
        loginResponse.setLastAccessedTime(tokenModel.getLastAccessedTime());
        return loginResponse;
    }

    /**
     * 退出
     *
     * @author Mr.Zhang
     * @since 2020-04-28
     */
    @Override
    public void logout() {
        try {
            tokenManager.deleteToken();
        } catch (Exception e) {
            log.warn("退出异常", e);
        }
    }

    @Override
    public Boolean updatePassword(String oldPwd, String newPwd) {
        LoginUsername loginUser = tokenManager.getLoginUser();
        log.info("loginUser:{}", loginUser);
        return chatUserService.updatePassword(loginUser.getUid(), oldPwd, newPwd);
    }

    @Override
    public LoginResponse registerAndLogin(String phone, String password, String nickname) {

        ChatUser newUser = chatUserService.addNewUser(phone, password, nickname);
        redisUtil.set(CommConstant.CHAT_USER_FIRST_KEY + newUser.getId(), "1");
        return getLoginResponseAndToken(newUser);
    }

}

