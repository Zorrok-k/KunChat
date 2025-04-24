package com.Kun.KunChat.common;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static com.Kun.KunChat.StaticVariable.StaticVariable.*;

/**
 * Author: Beta
 * Date: 2025/4/23 9:57
 * Param:
 * Return:
 * Description: 一个自定义工具类
 **/

@Component
public class CustomizeUtils {

    // 获取一个UUID默认16位
    public String getUUID(int size) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, size);
    }

    public String getUUID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    // 上传文件
    public void upLoad(MultipartFile file, String id, int type) throws IOException {
        File targetFileFolder = switch (type) {
            // 传用户头像
            case 0 -> new File(USER_PATH + id + "/picture", USER_AVATAR_NAME);
            // 传用户信息背景图
            case 1 -> new File(USER_PATH + id + "/picture", USERINFO_COVER_NAME);
            // 传群组头像
            case 2 -> new File(GROUP_PATH + id + "/picture", GROUP_AVATAR_NAME);
            // 传群组信息背景图
            case 3 -> new File(GROUP_PATH + id + "/picture", GROUP_COVER_NAME);
            default -> null;
        };
        // 目标路径不存在就要创建
        if (!targetFileFolder.exists()) {
            targetFileFolder.mkdirs();
        }
        file.transferTo(targetFileFolder);
    }
}
