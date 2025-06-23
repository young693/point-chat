package com.point.chat.pointadmin.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.point.chat.pointcommon.dto.ChatUserMsgDto;
import com.point.chat.pointcommon.model.ChatMsg;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 会话消息表数据Mapper
 */
public interface ChatMsgDao extends BaseMapper<ChatMsg> {

    @Select("select cm.id, cm.chat_id, cm.chat_type, cm.send_user_id,cm.to_user_id, cm.msg_type, cm.msg, cm.send_time, cm.timestamp, " +
            "       cmu.user_id, cmu.is_offline,cmu.is_collect,cmu.is_unread " +
            "from chat_msg cm " +
            "inner join chat_msg_user_rel cmu on cm.id = cmu.msg_id " +
            " ${ew.customSqlSegment} ")
    List<ChatUserMsgDto> selectUserMsgList(@Param(Constants.WRAPPER)QueryWrapper<ChatUserMsgDto> queryWrapper);
}
