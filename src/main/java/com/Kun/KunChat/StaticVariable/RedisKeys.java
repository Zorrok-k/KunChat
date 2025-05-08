package com.Kun.KunChat.StaticVariable;

import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * Author: Beta
 * Date: 2025/4/20 17:11
 * Description: 用于自定义Redis的key
 **/

@ToString
@Getter
@Component
public class RedisKeys {

    // 注册验证码的唯一标识
    public static final String CODESIGN = "CodeSign::";

    // 登录成功后登录信息在redis中的唯一标识
    public static final String LOGINID = "LoginId::";

    // 用户未读消息的标识
    public static final String UNREAD = "OfflineMessage::";

    // 私有化构造函数
    RedisKeys() {
    }

}
