package com.Kun.KunChat.common;

import lombok.Getter;
import lombok.ToString;

/**
 * Author: Beta
 * Date: 2025/4/19 12:06
 * Param:
 * Return:
 * Description: 写一些状态码
 **/

@ToString
@Getter
public enum Status {

    /**
     * 1000 大成功
     * 2000 大失败
     */

    SUCCEED(1000, "succeed"),
    ERROR_VALIDATED(1100, "参数不合法！"),
    ERROR_NULLPOINTER(1400,"空指针异常！"),
    ERROR_CHECKCODELOSE(1510,"验证码失效！请重新获取。"),
    ERROR_CHECKCODEWRONG(1511,"验证码错误！请重新输入。"),
    ERROR_LOGIN(1610,"邮箱或密码错误！"),
    ERROR_LOGINLOSE(1611,"登录已失效！"),
    ERROR_LOGINOUT(1612,"登录已失效！无需退出！"),
    ERROR_ADMIN(1613,"你并非管理员！"),
    ERROR_EMAILEXITS(1520,"邮箱已存在！"),
    ERROR_USERIDEXITS(1521,"ID已存在！"),
    ERROR_BUSINESS(1500,"业务异常！"),
    FAILED(2000,"服务器异常！请稍后尝试。");

    private Integer code;
    private String message;

    // 私有化构造器
    Status() {}

    Status(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
