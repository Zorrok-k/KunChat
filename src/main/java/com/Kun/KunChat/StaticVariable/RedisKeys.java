package com.Kun.KunChat.StaticVariable;

import lombok.Getter;
import lombok.ToString;

/**
 * Author: Beta
 * Date: 2025/4/20 17:11
 * Description: 用于自定义Redis的key
 **/

@ToString
@Getter
public enum RedisKeys {

    CODESIGN("CodeSign::"),
    LOGINID("LoginId::");

    private String key;

    RedisKeys() {
    }


    RedisKeys(String key) {
        this.key = key;
    }

}
