package com.Kun.KunChat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Kun.KunChat.entity.UserInfo;
import com.Kun.KunChat.service.UserInfoService;
import com.Kun.KunChat.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author Kun
* @description 针对表【user_info(用户信息)】的数据库操作Service实现
* @createDate 2025-04-17 17:00:52
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




