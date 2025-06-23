package com.point.chat.pointadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.point.chat.pointcommon.dto.ChatUserSimDto;
import com.point.chat.pointcommon.model.ChatUser;
import com.point.chat.pointcommon.response.ChatUserInfoResponse;
import com.point.chat.pointcommon.response.ChatUserResponse;
import com.point.chat.pointcommon.response.ChatUserSearchResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 聊天室用户表(关联管理员表和企业用户表)服务接口
 */
public interface ChatUserService extends IService<ChatUser> {

    List<ChatUser> getList(List<Integer> userIds);

    List<ChatUser> getSimList(List<Integer> userIds);

    List<ChatUserSimDto> getChatUserLeftFriendSimList(List<Integer> userIds, Integer currentUserId);

    List<Integer> getAllChatUserIds();

    ChatUser getByPhone(String phone);

    /**
     * 根据用户id和类型获取聊天室用户
     *
     * @param userId   用户ID
     * @return ChatUser
     **/
    ChatUser getChatUser(Integer userId);

    /**
     * 根据用户id和类型获取聊天室用户
     *
     * @param userId   用户ID
     * @return ChatUser
     **/
    Integer getChatUserId(Integer userId);

    /**
     * 获取当前聊天室用户
     *
     * @return ChatUser
     */
    ChatUserResponse getCurrentChatUser();

    /**
     * 获取当前聊天室用户ID
     *
     * @return ChatUser
     */
    Integer getCurrentChatUserId();

    String getNicknameById(Integer id);

    /**
     * 更新聊天室用户在线状态
     *
     * @param chatUserId 聊天室用户ID
     * @param isOnline   是否在线
     * @return boolean
     */
    boolean updateOnlineStatus(Integer chatUserId, boolean isOnline);

    /**
     * 获取聊天室用户列表-不包含好友(用户搜索添加)
     *
     * @param keyword  关键词
     * @return 用户列表
     */
    List<ChatUserSearchResponse> getSearchChatUserList(String keyword);

    List<ChatUserSearchResponse> getSearchFriendList(String keyword);

    ChatUserInfoResponse getChatUserInfo(Integer userId);

    List<Integer> getAllUserIds();

    /**
     * 更新聊天室用户头像
     *
     * @param imgFile  图片文件
     */
    String updateAvatar(MultipartFile imgFile);

    /**
     * 更新聊天室用户昵称
     *
     * @param nickname 昵称
     * @param sex      性别
     * @param signature 个性签名
     * @return boolean
     */
    Boolean updateNickname(String nickname, Integer sex, String signature);

    Boolean updateAvatarByUrl(String url);

    void updateLastLoginIpTime(Integer uid);

    Boolean updatePassword(Integer uid, String oldPwd, String newPwd);

    boolean isExist(String phone);

    ChatUser addNewUser(String phone, String password, String nickname);
}
