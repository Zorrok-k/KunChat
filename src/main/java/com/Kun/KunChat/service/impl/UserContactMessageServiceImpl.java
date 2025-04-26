package com.Kun.KunChat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Kun.KunChat.entity.UserContactMessage;
import com.Kun.KunChat.service.UserContactMessageService;
import com.Kun.KunChat.mapper.UserContactMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Kun
 * @description 针对表【user_contact_message】的数据库操作Service实现
 * @createDate 2025-04-26 11:38:06
 */
@Service
public class UserContactMessageServiceImpl extends ServiceImpl<UserContactMessageMapper, UserContactMessage>
        implements UserContactMessageService {

    @Autowired
    private UserContactMessageMapper userContactMessageMapper;

    @Override
    public UserContactMessage addMessage(UserContactMessage userContactMessage) {
        userContactMessageMapper.insert(userContactMessage);
        return userContactMessageMapper.selectById(userContactMessage.getId());
    }

    @Override
    public UserContactMessage getMessage(String applicantId, String contactId) {
        return userContactMessageMapper.selectOne(new QueryWrapper<UserContactMessage>()
                .eq("applicant_id", applicantId).eq("contact_id", contactId));
    }

    @Override
    public UserContactMessage updateMessage(UserContactMessage userContactMessage) {
        userContactMessageMapper.updateById(userContactMessage);
        return userContactMessageMapper.selectById(userContactMessage.getId());
    }
}




