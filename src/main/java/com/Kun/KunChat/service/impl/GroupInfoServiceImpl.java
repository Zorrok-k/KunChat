package com.Kun.KunChat.service.impl;

import com.Kun.KunChat.common.CustomizeUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Kun.KunChat.entity.GroupInfo;
import com.Kun.KunChat.service.GroupInfoService;
import com.Kun.KunChat.mapper.GroupInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Kun
 * @description 针对表【group_info】的数据库操作Service实现
 * @createDate 2025-04-24 10:27:46
 */
@Service
public class GroupInfoServiceImpl extends ServiceImpl<GroupInfoMapper, GroupInfo> implements GroupInfoService {

    @Autowired
    private GroupInfoMapper groupInfoMapper;

    @Autowired
    private CustomizeUtils customizeUtils;

    @Override
    public GroupInfo createGroup(String userId, String groupName, int joinType) {
        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setGroupId(customizeUtils.getUUID());
        groupInfo.setGroupName(groupName);
        groupInfo.setOwnerId(userId);
        groupInfo.setJoinType(joinType);
        // 生成当前时间
        LocalDateTime timeNow = LocalDateTime.now();
        groupInfo.setCreateTime(timeNow);
        // 写入数据库
        groupInfoMapper.insert(groupInfo);

        return groupInfoMapper.selectById(groupInfo.getGroupId());
    }
}




