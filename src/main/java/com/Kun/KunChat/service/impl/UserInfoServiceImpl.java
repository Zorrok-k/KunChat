package com.Kun.KunChat.service.impl;

import com.Kun.KunChat.StaticVariable.RedisKeys;
import com.Kun.KunChat.common.CustomizeUtils;
import com.Kun.KunChat.entity.UserInfo;
import com.Kun.KunChat.mapper.UserInfoMapper;
import com.Kun.KunChat.service.RedisService;
import com.Kun.KunChat.service.UserInfoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author Kun
 * @description 针对表【user_info(用户信息)】的数据库操作Service实现
 * @createDate 2025-04-17 17:00:52
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CustomizeUtils customizeUtils;

    @Override
    public UserInfo checkEmail(String email) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", email);
        // log.error("检查邮箱是否存在：{}", email);
        return userInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public UserInfo checkUserId(String userId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return userInfoMapper.selectOne(queryWrapper);
    }

    @Override
    public UserInfo addUser(String nikeName, String email, String password) {
        UserInfo user = new UserInfo();
        user.setUserId(customizeUtils.getUUID());
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
    public UserInfo getUser(String id) {
        log.info("缓存未命中id: {}，执行查询", id);
        UserInfo user = userInfoMapper.selectById(id);
        user.setPassword("******");
        return user;
    }

    @Override
    public <T> Page<UserInfo> getUser(String nikeName, int page) {
        Page<UserInfo> thePage = new Page<>(page, 10, true);
        userInfoMapper.selectPage(thePage, new QueryWrapper<UserInfo>().like("nick_name", nikeName).or().eq("nick_name", nikeName));
        return thePage;
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
        // 更新上线时间
        user = userInfoMapper.selectById(user.getUserId());
        // 同时更新缓存数据，如果有
        if (redisService.hasKey("UserInfo::" + user.getUserId())) {
            redisService.setValue("UserInfo::" + user.getUserId(), user, redisService.getValueTTL("UserInfo::" + user.getUserId()));
        }
        return user;
    }

    @Transactional
    @Override
    public void loginOut(String loginId, String userId) {
        // 注意，这里删的应该是登录凭证而非用户信息的缓存
        redisService.delete(RedisKeys.LOGINID.getKey() + loginId);
        UserInfo user = new UserInfo();
        user.setUserId(userId);
        // 生成当前时间
        LocalDateTime timeNow = LocalDateTime.now();
        // 将时间转换为毫秒
        user.setLastOffTime(timeNow.toInstant(ZoneOffset.of("+8")).toEpochMilli());
        // 更新下线时间
        userInfoMapper.updateById(user);
    }

    @Transactional
    @Override
    public UserInfo updateUser(UserInfo user) {
        if (user.getUserId() == null) {
            return null;
        }
        userInfoMapper.updateById(user);
        user = userInfoMapper.selectById(user.getUserId());
        // 同时更新缓存数据，如果有
        if (redisService.hasKey("UserInfo::" + user.getUserId())) {
            redisService.setValue("UserInfo::" + user.getUserId(), user, redisService.getValueTTL("UserInfo::" + user.getUserId()));
        }
        return user;
    }


}




