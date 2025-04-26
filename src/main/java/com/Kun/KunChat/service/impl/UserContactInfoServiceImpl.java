package com.Kun.KunChat.service.impl;

import com.Kun.KunChat.common.CustomizeUtils;
import com.Kun.KunChat.entity.UserContactInfo;
import com.Kun.KunChat.mapper.UserContactInfoMapper;
import com.Kun.KunChat.service.UserContactInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kun
 * @description 针对表【user_contact_info】的数据库操作Service实现
 * @createDate 2025-04-24 11:59:43
 */
@Service
public class UserContactInfoServiceImpl extends ServiceImpl<UserContactInfoMapper, UserContactInfo>
        implements UserContactInfoService {

    @Autowired
    private UserContactInfoMapper userContactInfoMapper;

    @Autowired
    private CustomizeUtils customizeUtils;

    @Override
    public UserContactInfo buildContact(String userId, String contactId, int type, int status) {
        // 生成当前时间
        LocalDateTime timeNow = LocalDateTime.now();
        UserContactInfo userContactInfo = new UserContactInfo();
        userContactInfo.setId(customizeUtils.getUUID());
        userContactInfo.setUserId(userId);
        userContactInfo.setContactId(contactId);
        userContactInfo.setType(type);
        userContactInfo.setStatus(status);
        userContactInfo.setCreateTime(timeNow);
        userContactInfoMapper.insert(userContactInfo);
        return userContactInfoMapper.selectById(userContactInfo.getId());
    }

    @Override
    public UserContactInfo updateStatus(String userId, String contactId, int status) {
        String id = userContactInfoMapper.selectOne(new QueryWrapper<UserContactInfo>()
                .eq("user_id", userId).eq("contact_id", contactId)).getId();
        UserContactInfo userContactInfo = new UserContactInfo();
        userContactInfo.setId(id);
        userContactInfo.setStatus(status);
        userContactInfoMapper.updateById(userContactInfo);
        return userContactInfoMapper.selectById(id);
    }

    @Override
    public List<String> getUserContactInfo(String userId, int type) {
        List<String> userContactInfoListId = new ArrayList<>();
        if (type == 0) {
            List<UserContactInfo> userContactInfoList = userContactInfoMapper.selectList(new QueryWrapper<UserContactInfo>()
                    .eq("user_id", userId).eq("type", 0));
            for (UserContactInfo userContactInfo : userContactInfoList) {
                userContactInfoListId.add(userContactInfo.getContactId());
            }
        } else {
            List<UserContactInfo> userContactInfoList = userContactInfoMapper.selectList(new QueryWrapper<UserContactInfo>()
                    .eq("user_id", userId).eq("type", 1));
            for (UserContactInfo userContactInfo : userContactInfoList) {
                userContactInfoListId.add(userContactInfo.getContactId());
            }
        }
        return userContactInfoListId;
    }
}




