package com.point.chat.pointadmin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.point.chat.pointcommon.dto.ChatGroupInfoDto;
import com.point.chat.pointcommon.dto.ChatGroupMemberSimDto;
import com.point.chat.pointcommon.model.ChatGroupMember;

import java.util.List;

/**
 * 聊天室群成员表服务接口
 */
public interface ChatGroupMemberService extends IService<ChatGroupMember> {

    /**
     * 获取用户加入的群组列表
     *
     * @param userId 用户ID
     * @return 群组列表
     */
    List<ChatGroupMember> getChatGroupMemberListByUserId(Integer userId);

    /**
     * 获取群组成员列表
     *
     * @param groupId 群组ID
     * @return 群组成员列表
     */
    List<ChatGroupMember> getChatGroupMemberListByGroupId(Integer groupId);

    /**
     * 获取群组成员ID列表
     *
     * @param groupId 群组ID
     * @return 群组成员列表
     */
    List<Integer> getChatGroupMemberIdListByGroupId(Integer groupId);

    /**
     * 获取群组成员ID列表
     *
     * @param groupId 群组ID
     * @return 群组成员列表
     */
    List<Integer> getChatGroupMemberIdListByGroupId(Integer groupId, List<Integer> userIds);

    /**
     * 获取群组成员精简列表
     *
     * @param groupId  群组ID
     * @param keywords 关键字
     * @return 群组成员列表
     */
    List<ChatGroupMemberSimDto> getChatGroupMemberSimList(Integer groupId, String keywords);

    /**
     * 获取群组成员精简列表
     *
     * @param groupId  群组ID
     * @param userId   用户类型
     * @param keywords 关键字
     * @return 群组成员列表
     */
    List<ChatGroupMemberSimDto> getChatGroupMemberList(Integer userId, Integer groupId, String keywords);

    /**
     * 获取群组成员头像列表
     *
     * @param groupId 群组ID
     * @return 群组成员头像列表
     */
    List<String> getGroupMemberAvaterList(Integer groupId);

    /**
     * 获取群组详情
     *
     * @param groupId 分组ID
     * @param userId  用户ID
     * @return 群组详情
     */
    ChatGroupInfoDto getChatGroupInfo(Integer groupId, Integer userId);
}
