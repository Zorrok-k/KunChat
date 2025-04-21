package com.Kun.KunChat.service.impl;

import com.Kun.KunChat.service.RedisService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.Kun.KunChat.entity.UserInfo;
import com.Kun.KunChat.service.UserInfoService;
import com.Kun.KunChat.mapper.UserInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * @author Kun
 * @description 针对表【user_info(用户信息)】的数据库操作Service实现
 * @createDate 2025-04-17 17:00:52
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
        implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisService redisService;

    @Override
    public UserInfo checkEmail(String email) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        // log.error("检查邮箱是否存在：{}", email);
        return userInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public UserInfo addUser(String nikeName, String email, String password) {
        UserInfo user = new UserInfo();
        user.setUserId(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        user.setNickName(nikeName);
        user.setEmail(email);
        user.setPassword(password);
        // 生成当前时间
        LocalDateTime timeNow = LocalDateTime.now();
        user.setCreateTime(timeNow);
        // 将时间转换为毫秒
        user.setLastOffTime(timeNow.toInstant(ZoneOffset.of("+8")).toEpochMilli());

        // 写入数据库
        userInfoMapper.insert(user);

        return userInfoMapper.selectById(user.getUserId());
    }

    @Cacheable(value = "UserInfo", key = "#id", cacheManager = "CacheManager_User")
    @Override
    public UserInfo getUserById(String id) {
        log.info("缓存未命中，查询中");
        UserInfo user = userInfoMapper.selectById(id);
        user.setPassword("******");
        return user;
    }

    @Transactional
    @Override
    public UserInfo login(String email, String password) {
        UserInfo user = new UserInfo();
        user = userInfoMapper.selectOne(new QueryWrapper<UserInfo>().eq("email", email));
        if (!user.getPassword().equals(password)) {
            return null;
        }
        // 通过后更新登录时间
        String userId = user.getUserId();
        user.setUserId(userId);
        // 生成当前时间
        LocalDateTime timeNow = LocalDateTime.now();
        user.setLastLoginTime(timeNow);
        userInfoMapper.updateById(user);
        user = userInfoMapper.selectById(user.getUserId());
        // 同时更新缓存数据
        if (redisService.hasKey("UserInfo::" + user.getUserId())) {
            redisService.setValue("UserInfo::" + user.getUserId(), user);
        }
        return user;
    }

    @Override
    public UserInfo updateUser(UserInfo user) {
        if (user.getUserId() == null) {
            return null;
        }
        userInfoMapper.updateById(user);
        return userInfoMapper.selectById(user.getUserId());
    }

}




