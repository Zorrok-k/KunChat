package com.Kun.KunChat.service;

import com.Kun.KunChat.entity.UserContactInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author Kun
 * @description 针对表【user_contact_info】的数据库操作Service
 * @createDate 2025-04-24 11:59:43
 */
public interface UserContactInfoService extends IService<UserContactInfo> {

    UserContactInfo buildContact(String userId, String contactId, int type, int status);

    UserContactInfo updateStatus(String userId, String contactId, int status);

    List<String> getUserContactInfo(String userId);
}
