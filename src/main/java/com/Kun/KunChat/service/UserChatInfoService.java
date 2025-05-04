package com.Kun.KunChat.service;

import com.Kun.KunChat.entity.UserChatInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Kun
 * @description 针对表【user_chat_info】的数据库操作Service
 * @createDate 2025-05-04 15:24:05
 */
public interface UserChatInfoService extends IService<UserChatInfo> {

    UserChatInfo addUserChatInfo(UserChatInfo userChatInfo);

    <T> Page<UserChatInfo> getMessages(String senderId, String receiverId, int page, int type);

    void updateUserChatInfo(String id, int status);

}
