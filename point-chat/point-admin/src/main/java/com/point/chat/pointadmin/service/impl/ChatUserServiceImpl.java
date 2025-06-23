package com.point.chat.pointadmin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.point.chat.pointadmin.dao.ChatFriendDao;
import com.point.chat.pointadmin.dao.ChatUserDao;
import com.point.chat.pointadmin.service.ChatUserService;
import com.point.chat.pointadmin.service.UploadService;
import com.point.chat.pointcommon.constants.CommConstant;
import com.point.chat.pointcommon.dto.ChatUserFriendDto;
import com.point.chat.pointcommon.dto.ChatUserSimDto;
import com.point.chat.pointcommon.em.ChatSourceEm;
import com.point.chat.pointcommon.em.ExceptionCodeEm;
import com.point.chat.pointcommon.entity.LoginUsername;
import com.point.chat.pointcommon.exception.ApiException;
import com.point.chat.pointcommon.manager.TokenManager;
import com.point.chat.pointcommon.model.ChatFriend;
import com.point.chat.pointcommon.model.ChatUser;
import com.point.chat.pointcommon.response.ChatUserInfoResponse;
import com.point.chat.pointcommon.response.ChatUserResponse;
import com.point.chat.pointcommon.response.ChatUserSearchResponse;
import com.point.chat.pointcommon.response.UploadResponse;
import com.point.chat.pointcommon.utils.CommUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 聊天室用户表(关联管理员表和企业用户表)服务接口实现
 */
@Slf4j
@Service
public class ChatUserServiceImpl extends ServiceImpl<ChatUserDao, ChatUser> implements ChatUserService {

    @Resource
    private ChatFriendDao chatFriendDao;

    @Resource
    private UploadService uploadService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private TokenManager tokenManager;

    private final Lock lock = new ReentrantLock();

    @Override
    public List<ChatUser> getList(List<Integer> userIds) {
        if (ObjectUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(ChatUser::getId, userIds);
        return this.list(queryWrapper);
    }

    @Override
    public List<ChatUser> getSimList(List<Integer> userIds) {
        if (ObjectUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(ChatUser::getId, ChatUser::getNickname, ChatUser::getAvatar, ChatUser::getIsOnline);
        queryWrapper.in(ChatUser::getId, userIds);
        return this.list(queryWrapper);
    }

    @Override
    public List<ChatUserSimDto> getChatUserLeftFriendSimList(List<Integer> userIds, Integer currentUserId) {
        if (ObjectUtil.isEmpty(userIds)) {
            return new ArrayList<>();
        }
        return baseMapper.selectChatUserLeftFriendSimList(CollUtil.join(userIds, ","), currentUserId);
    }

    @Override
    public List<Integer> getAllChatUserIds() {
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(ChatUser::getId);
        return this.listObjs(queryWrapper, obj -> (Integer) obj);
    }

    @Override
    public ChatUser getByPhone(String phone) {
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ChatUser::getPhone, phone);
        return this.getOne(queryWrapper);
    }

    @Override
    public ChatUser getChatUser(Integer userId) {
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ChatUser::getId, userId);
        return this.getOne(queryWrapper);
    }

    @Override
    public Integer getChatUserId(Integer userId) {
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(ChatUser::getId);
        queryWrapper.eq(ChatUser::getId, userId);
        ChatUser one = this.getOne(queryWrapper);
        return ObjectUtil.isNotNull(one) ? one.getId() : null;
    }

    @Override
    public ChatUserResponse getCurrentChatUser() {
        LoginUsername loginUser = tokenManager.getLoginUser();
        ChatUser user = this.getChatUser(loginUser.getUid());
        return BeanUtil.copyProperties(user, ChatUserResponse.class);
    }

    @Override
    public Integer getCurrentChatUserId() {
        return tokenManager.getLoginUser().getUid();
    }

    @Override
    public String getNicknameById(Integer id) {
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(ChatUser::getNickname);
        queryWrapper.eq(ChatUser::getId, id);
        ChatUser one = this.getOne(queryWrapper);
        if (ObjectUtil.isNotNull(one)) {
            return one.getNickname();
        }
        return null;
    }

    @Override
    public boolean updateOnlineStatus(Integer chatUserId, boolean isOnline) {
        LambdaUpdateWrapper<ChatUser> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(ChatUser::getId, chatUserId)
                .eq(ChatUser::getIsOnline, !isOnline)
                .set(ChatUser::getIsOnline, isOnline);
        return this.update(updateWrapper);
    }

    @Override
    public List<ChatUserSearchResponse> getSearchChatUserList(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return new ArrayList<>(0);
        }

        // SELECT friend_id FROM chat_friend WHERE user_id = ?
        List<Integer> friendIds = getFriendIds();

        //SELECT id, nickname, avatar, sex, signature FROM chat_user
        //WHERE id NOT IN (friendIds) AND (nickname LIKE '%keyword%' OR phone LIKE '%keyword%') LIMIT 10
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(ChatUser::getId, ChatUser::getNickname, ChatUser::getAvatar,ChatUser::getSex,ChatUser::getSignature);
        queryWrapper.notIn(ChatUser::getId, friendIds);
        queryWrapper.and(wrapper -> {
            wrapper.like(ChatUser::getNickname, keyword);
            wrapper.or().like(StringUtils.isNumeric(keyword), ChatUser::getPhone, keyword);
        });
        queryWrapper.last("limit 10");
        List<ChatUser> chatUserList = this.list(queryWrapper);
        return BeanUtil.copyToList(chatUserList, ChatUserSearchResponse.class);
    }

    @Override
    public List<ChatUserSearchResponse> getSearchFriendList(String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return new ArrayList<>(0);
        }

        // SELECT friend_id FROM chat_friend WHERE user_id = ?
        List<Integer> friendIds = getFriendIds();

        //SELECT id, nickname, avatar, sex, signature FROM chat_user
        //WHERE id NOT IN (friendIds) AND (nickname LIKE '%keyword%' OR phone LIKE '%keyword%') LIMIT 10
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(ChatUser::getId, ChatUser::getNickname, ChatUser::getAvatar,ChatUser::getSex,ChatUser::getSignature);
        queryWrapper.in(ChatUser::getId, friendIds);
        queryWrapper.and(wrapper -> {
            wrapper.like(ChatUser::getNickname, keyword);
            wrapper.or().like(StringUtils.isNumeric(keyword), ChatUser::getPhone, keyword);
        });
        queryWrapper.last("limit 10");
        List<ChatUser> chatUserList = this.list(queryWrapper);
        return BeanUtil.copyToList(chatUserList, ChatUserSearchResponse.class);
    }

    private List<Integer> getFriendIds() {
        Integer chatUserId = this.getCurrentChatUserId();
        List<Integer> friendIds = getFriendIds(chatUserId);
        friendIds.add(chatUserId);
        return friendIds;
    }

    private List<Integer> getFriendIds(Integer userId) {
        LambdaQueryWrapper<ChatFriend> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ChatFriend::getUserId, userId).select(ChatFriend::getFriendId);
        return chatFriendDao.selectObjs(queryWrapper);
    }

    @Override
    public ChatUserInfoResponse getChatUserInfo(Integer userId) {
        ChatUserResponse chatUser = this.getCurrentChatUser();
        ChatUserInfoResponse response = new ChatUserInfoResponse();
        if (chatUser.getId().equals(userId)) {
            response.setIsFriend(true);
            BeanUtil.copyProperties(chatUser, response);
            response.setUserId(userId);
            return response;
        }
        ChatUserFriendDto chatUserFriendDto = chatFriendDao.selectChatUserFriendInfo(chatUser.getId(), userId);
        if (ObjectUtil.isNotNull(chatUserFriendDto)) {
            BeanUtil.copyProperties(chatUserFriendDto, response);
            response.setIsFriend(true);
            response.setUserId(userId);
            response.setSourceDesc(ChatSourceEm.getDesc(chatUserFriendDto.getSource()));
        } else {
            log.warn("用户非好友,chatUserId:{},userId:{}", chatUser.getId(), userId);
            ChatUser user = getById(userId);
            if (ObjectUtil.isNull(user)) {
                log.error("用户不存在,userId:{}", userId);
                throw new ApiException(ExceptionCodeEm.NOT_FOUND, "用户不存在");
            }
            BeanUtil.copyProperties(user, response);
            response.setIsFriend(false);
            response.setUserId(userId);
        }
        return response;
    }

    @Override
    public List<Integer> getAllUserIds() {
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(ChatUser::getId);
        List<ChatUser> chatUserList = this.list(queryWrapper);
        return chatUserList.stream().map(ChatUser::getId).collect(Collectors.toList());
    }

    @Override
    public String updateAvatar(MultipartFile imgFile) {
        if (ObjectUtil.isNull(imgFile)) {
            log.error("上传头像失败,imgFile为空");
            throw new ApiException(ExceptionCodeEm.PRAM_NOT_MATCH, "请选择头像文件");
        }
        Integer chatUserId = getCurrentChatUserId();
        UploadResponse uploadResponse = uploadService.uploadImage(imgFile, "chat-msg");

        // update chat_user set avator = url where id = chat_user_id
        LambdaUpdateWrapper<ChatUser> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(ChatUser::getId, chatUserId);
        updateWrapper.set(ChatUser::getAvatar, uploadResponse.getUrl());
        if (this.update(updateWrapper)) {
            return uploadResponse.getUrl();
        }
        log.error("更新头像失败,chatUserId:{}", chatUserId);
        throw new ApiException(ExceptionCodeEm.SYSTEM_ERROR, "上传头像失败");
    }

    @Override
    public Boolean updateNickname(String nickname, Integer sex,String signature) {
        Integer chatUserId = getCurrentChatUserId();
        LambdaUpdateWrapper<ChatUser> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(ChatUser::getId, chatUserId);
        updateWrapper.set(ChatUser::getNickname, nickname);
        updateWrapper.set(ChatUser::getSex, sex);
        updateWrapper.set(ChatUser::getSignature, signature);
        return this.update(updateWrapper);
    }

    @Override
    public Boolean updateAvatarByUrl(String url){
        Integer chatUserId = getCurrentChatUserId();
        LambdaUpdateWrapper<ChatUser> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(ChatUser::getId, chatUserId);
        updateWrapper.set(ChatUser::getAvatar, url);
        return this.update(updateWrapper);
    }

    @Override
    public void updateLastLoginIpTime(Integer uid) {
        LambdaUpdateWrapper<ChatUser> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.eq(ChatUser::getId, uid);
        updateWrapper.set(ChatUser::getLastLoginTime, DateUtil.now());
        updateWrapper.set(ChatUser::getLastIp, CommUtil.getClientIp());
        this.update(updateWrapper);
    }

    @Override
    public Boolean updatePassword(Integer uid, String oldPwd, String newPwd) {
        ChatUser user = getById(uid);
        checkPwd(user.getPwd(), oldPwd, newPwd, user.getPhone());
        LambdaUpdateWrapper<ChatUser> lqw = Wrappers.lambdaUpdate();
        lqw.eq(ChatUser::getId, uid);
        lqw.set(ChatUser::getPwd, passwordEncoder.encode(newPwd));
        return this.update(lqw);
    }


    private void checkPwd(String pwd, String oldPwd, String newPwd, String account) {
        if(!passwordEncoder.matches(oldPwd, pwd)){
            log.error("原密码错误,account:{}", account);
            throw new ApiException(ExceptionCodeEm.VALIDATE_FAILED, "原密码错误");
        }
        if(passwordEncoder.matches(newPwd, pwd)){
            log.error("新密码不能与原密码相同,account:{}", account);
            throw new ApiException(ExceptionCodeEm.VALIDATE_FAILED, "新密码不能与原密码相同");
        }
    }

    @Override
    public boolean isExist(String phone) {
        LambdaQueryWrapper<ChatUser> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(ChatUser::getId);
        queryWrapper.eq(ChatUser::getPhone, phone);
        queryWrapper.last("limit 1");
        return ObjectUtil.isNotNull(this.getOne(queryWrapper));
    }

    @Override
    public ChatUser addNewUser(String phone, String password, String nickname) {
        if (isExist(phone)) {
            log.error("账号已存在,phone:{}", phone);
            throw new ApiException(ExceptionCodeEm.VALIDATE_FAILED, "账号已存在");
        }
        ChatUser newUser = new ChatUser();
        newUser.setNickname(StringUtils.isBlank(nickname) ? phone : nickname);
        newUser.setPhone(phone);
        newUser.setPwd(passwordEncoder.encode(password));
        log.info("password:{}", newUser.getPwd());
        newUser.setAvatar(CommConstant.DEFAULT_AVATAR);
        boolean saved = this.save(newUser);
        if (!saved) {
            log.error("注册失败,newUser:{}", newUser);
            throw new ApiException(ExceptionCodeEm.SYSTEM_ERROR, "注册失败");
        }
        log.info("注册成功！");
        return newUser;
    }
}
