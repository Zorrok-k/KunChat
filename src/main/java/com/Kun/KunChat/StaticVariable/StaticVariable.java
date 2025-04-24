package com.Kun.KunChat.StaticVariable;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Author: Beta
 * Date: 2025/4/23 16:36
 * Description: 文件路径静态变量
 **/

@Component
public class StaticVariable {

    // 用户静态资源路径
    public static String USER_PATH = null;
    public static final String USER_AVATAR_NAME = "avatar.jpg";
    public static final String USERINFO_COVER_NAME = "userinfo_cover.jpg";
    // 群组静态资源路径
    public static String GROUP_PATH = null;
    public static final String GROUP_AVATAR_NAME = "avatar.jpg";
    public static final String GROUP_COVER_NAME = "group_cover.jpg";

    StaticVariable() {
    }

    @Value("${spring.web.resources.static-locations}")
    private void init(String basePath) {
        USER_PATH = basePath.replace("file:", "") + "Users/";
        GROUP_PATH = basePath.replace("file:", "") + "Groups/";
    }

}
