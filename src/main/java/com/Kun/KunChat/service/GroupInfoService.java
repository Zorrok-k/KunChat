package com.Kun.KunChat.service;

import com.Kun.KunChat.entity.GroupInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Kun
 * @description 针对表【group_info】的数据库操作Service
 * @createDate 2025-04-24 10:27:46
 */
public interface GroupInfoService extends IService<GroupInfo> {

    GroupInfo createGroup(String userId, String groupName, int joinType);

    GroupInfo getGroupInfo(String groupId);

    <T> Page<GroupInfo> getGroupInfo(String groupName, int page);

    GroupInfo updateGroupInfo(GroupInfo groupInfo);

}
