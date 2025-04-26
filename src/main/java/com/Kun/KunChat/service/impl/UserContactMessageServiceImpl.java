package com.Kun.KunChat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Kun.KunChat.entity.UserContactMessage;
import com.Kun.KunChat.service.UserContactMessageService;
import com.Kun.KunChat.mapper.UserContactMessageMapper;
import org.springframework.stereotype.Service;

/**
* @author Kun
* @description 针对表【user_contact_message】的数据库操作Service实现
* @createDate 2025-04-26 11:33:40
*/
@Service
public class UserContactMessageServiceImpl extends ServiceImpl<UserContactMessageMapper, UserContactMessage>
    implements UserContactMessageService{

}




