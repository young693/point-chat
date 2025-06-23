package com.point.chat.pointadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.point.chat.pointadmin.dao.ChatRoomUserRelDao;
import com.point.chat.pointadmin.service.ChatRoomUserRelService;
import com.point.chat.pointcommon.model.ChatRoomUserRel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 聊天室和用户关联表服务接口实现
 */
@Slf4j
@Service
public class ChatRoomUserRelServiceImpl extends ServiceImpl<ChatRoomUserRelDao, ChatRoomUserRel> implements ChatRoomUserRelService {

    @Override
    public List<ChatRoomUserRel> getChatRoomUserRelListByChatId(String chatId) {
        LambdaQueryWrapper<ChatRoomUserRel> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ChatRoomUserRel::getChatId, chatId);
        return this.list(queryWrapper);
    }

    @Override
    public ChatRoomUserRel getChatRoomUserRel(String chatId, Integer userId) {
        LambdaQueryWrapper<ChatRoomUserRel> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(ChatRoomUserRel::getChatId, chatId);
        queryWrapper.eq(ChatRoomUserRel::getUserId, userId);
        return this.getOne(queryWrapper);
    }
}
