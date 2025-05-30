package com.Kun.KunChat.service;

import com.Kun.KunChat.entity.UserContactMessage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Kun
 * @description 针对表【user_contact_message】的数据库操作Service
 * @createDate 2025-04-26 11:38:06
 */
public interface UserContactMessageService extends IService<UserContactMessage> {

    UserContactMessage addMessage(UserContactMessage userContactMessage);

    UserContactMessage getMessage(int id);

    UserContactMessage getMessage(String applicantId, String contactId);

    UserContactMessage updateMessage(UserContactMessage userContactMessage);

}
