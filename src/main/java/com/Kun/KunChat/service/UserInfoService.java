package com.Kun.KunChat.service;

import com.Kun.KunChat.entity.UserInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author Kun
 * @description 针对表【user_info(用户信息)】的数据库操作Service
 * @createDate 2025-04-17 17:00:52
 */

public interface UserInfoService extends IService<UserInfo> {

    UserInfo checkEmail(String email);

    UserInfo checkUserId(String userId);

    UserInfo addUser(String nikeName, String email, String password);

    UserInfo getUser(String id);

    <T> Page<UserInfo> getUser(String nikeName, int page);

    UserInfo login(String email, String password);

    void loginOut(String loginId, String userId);

    UserInfo updateUser(UserInfo user);

    // void upLoad(MultipartFile file, String userId, int type) throws IOException;

}
