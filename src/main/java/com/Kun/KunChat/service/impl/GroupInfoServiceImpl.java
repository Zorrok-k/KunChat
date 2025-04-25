package com.Kun.KunChat.service.impl;

import com.Kun.KunChat.common.CustomizeUtils;
import com.Kun.KunChat.entity.GroupInfo;
import com.Kun.KunChat.mapper.GroupInfoMapper;
import com.Kun.KunChat.service.GroupInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

    @Override
    public GroupInfo getGroupInfo(String groupId) {
        GroupInfo groupInfo = groupInfoMapper.selectById(groupId);
        if (groupInfo.getStatus() == 0) {
            return null;
        }
        return groupInfo;
    }

    @Override
    public <T> Page<GroupInfo> getGroupInfo(String groupName, int page) {
        // 彩蛋，用的还是做毕设时候的代码，哈哈
        Page<GroupInfo> thePage = new Page<>(page, 10, true);
        groupInfoMapper.selectPage(thePage, new QueryWrapper<GroupInfo>().like("group_name", groupName).eq("status", 1));
        return thePage;
    }

    @Override
    public GroupInfo updateGroupInfo(GroupInfo groupInfo) {
        if (groupInfo.getGroupId().isEmpty()) {
            return null;
        }
        groupInfoMapper.updateById(groupInfo);
        return groupInfoMapper.selectById(groupInfo.getGroupId());
    }

    @Override
    public GroupInfo deleteGroupInfo(String groupId) {
        GroupInfo groupInfo = groupInfoMapper.selectById(groupId);
        if (groupInfo != null) {
            groupInfo.setStatus(0);
            groupInfoMapper.updateById(groupInfo);
        }
        return groupInfoMapper.selectById(groupId);
    }
}




