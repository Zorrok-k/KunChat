package com.Kun.KunChat.common;

import org.springframework.stereotype.Component;

import java.util.UUID;

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

    public String getUUID(int size){
        return UUID.randomUUID().toString().replace("-", "").substring(0, size);
    }

    public String getUUID(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
