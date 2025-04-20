package com.Kun.KunChat.common;

import lombok.Getter;
import lombok.ToString;

/**
 * Author: Beta
 * Date: 2025/4/20 17:11
 * Param:
 * Return:
 * Description: 用于自定义Redis的key
 **/

@ToString
@Getter
public enum RedisKeys {

    CODESIGN("CodeSign:"),
    LOGINID("LoginID:");

    private String key;

    RedisKeys() {
    }


    RedisKeys(String key) {
        this.key = key;
    }

}
