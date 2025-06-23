package com.point.chat.pointadmin.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.point.chat.pointcommon.dto.ChatRoomMsgInfoDto;
import com.point.chat.pointcommon.dto.ChatRoomUserDto;
import com.point.chat.pointcommon.model.ChatRoom;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户聊天室表数据Mapper
 */
public interface ChatRoomDao extends BaseMapper<ChatRoom> {

    @Select("select cr.chat_id, cr.chat_type, cr.group_id, cru.last_msg_id, cru.last_msg, cru.last_time, cr.timestamp, cr.create_time, " +
            "       cru.id, user_id, is_top, msg_no_disturb, unread_msg_count, offline_msg_count " +
            "       from chat_room cr " +
            "inner join chat_room_user_rel cru on cr.chat_id=cru.chat_id " +
            " ${ew.customSqlSegment} ")
    List<ChatRoomUserDto> selectChatRoomUserList(@Param(Constants.WRAPPER) QueryWrapper<ChatRoomUserDto> queryWrapper);

    @Select("select cr.chat_id,cr.group_id,cr.chat_type,crur.is_top,crur.msg_no_disturb from chat_room cr " +
            "inner join chat_room_user_rel crur on cr.chat_id = crur.chat_id " +
            "where crur.chat_id=#{chatId} and crur.user_id=#{userId} ")
    ChatRoomMsgInfoDto selectChatRoomMsgInfo(@Param("chatId") String chatId, @Param("userId") Integer userId);
}
