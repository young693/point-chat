package com.point.chat.pointadmin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.point.chat.pointadmin.dao.ChatUserBlacklistDao;
import com.point.chat.pointadmin.service.ChatUserBlacklistService;
import com.point.chat.pointcommon.model.ChatUserBlacklist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 聊天室用户黑名单表服务接口实现
 */
@Slf4j
@Service
public class ChatUserBlacklistServiceImpl extends ServiceImpl<ChatUserBlacklistDao, ChatUserBlacklist> implements ChatUserBlacklistService {

}
