package com.point.chat.pointadmin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.point.chat.pointcommon.dto.ChatFriendApplyDto;
import com.point.chat.pointcommon.model.ChatFriendApply;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

/**
 * 聊天室新好友申请表数据Mapper
 */
public interface ChatFriendApplyDao extends BaseMapper<ChatFriendApply> {

    @Select("select cfa.id, cfa.apply_user_id,cfa.friend_id, cu.nickname,cfaur.remark,cu.avatar,cu.signature,cfaur.unread_count, cfa.status, cfa.apply_reply,cfa.apply_reason " +
            "from chat_friend_apply cfa " +
            "         inner join chat_friend_apply_user_rel cfaur on cfa.id = cfaur.apply_id " +
            "         inner join chat_user cu on cu.id = cfaur.friend_id " +
            "where cfaur.user_id=#{userId} " +
            "order by cfaur.id desc")
    List<ChatFriendApplyDto> selectChatFriendApplyList(@Param("userId") Integer userId);

    @Select("select cfa.id, cfa.apply_user_id,cfa.friend_id, cu.nickname,cfaur.remark,cu.avatar,cu.signature,cfaur.remark, cfa.status, cfaur.label,cfa.source ,cfaur.unread_count " +
            "from chat_friend_apply cfa " +
            "         inner join chat_friend_apply_user_rel cfaur on cfa.id = cfaur.apply_id " +
            "         inner join chat_user cu on cu.id = cfaur.friend_id " +
            "where cfa.id=#{applyId} and cfaur.user_id=#{userId}")
    ChatFriendApplyDto selectChatFriendApplyInfo(@Param("applyId") Integer applyId,@Param("userId") Integer userId);
}
