package com.Kun.KunChat.service.impl;

import com.Kun.KunChat.common.CustomizeUtils;
import com.Kun.KunChat.entity.UserChatInfo;
import com.Kun.KunChat.mapper.UserChatInfoMapper;
import com.Kun.KunChat.service.UserChatInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Kun
 * @description 针对表【user_chat_info】的数据库操作Service实现
 * @createDate 2025-05-04 15:24:05
 */
@Service
public class UserChatInfoServiceImpl extends ServiceImpl<UserChatInfoMapper, UserChatInfo>
        implements UserChatInfoService {

    @Autowired
    private CustomizeUtils customizeUtils;

    @Autowired
    private UserChatInfoMapper userChatInfoMapper;

    @Override
    public UserChatInfo addUserChatInfo(UserChatInfo userChatInfo) {
        // 生成消息的唯一id
        String id = customizeUtils.getUUID();
        // 生成当前时间
        LocalDateTime timeNow = LocalDateTime.now();
        userChatInfo.setId(id);
        userChatInfo.setCreateTime(timeNow.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        userChatInfoMapper.insert(userChatInfo);
        return userChatInfoMapper.selectById(id);
    }


    @Override
    public <T> Page<UserChatInfo> getMessages(String senderId, String receiverId, int page, int type) {
        Page<UserChatInfo> thePage = new Page<>(page, 35, true);
        userChatInfoMapper.selectPage(thePage, new QueryWrapper<UserChatInfo>().eq("sender_id", senderId)
                .eq("receiver_id", receiverId).eq("chat_type", type));
        return thePage;
    }

    @Override
    public void updateUserChatInfo(String id, int status) {

    }
}




