package com.Kun.KunChat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Beta
 * Date: 2025/4/23 10:44
 * Description:  aspect 的全局拦截自定义注解
 * 两个注解，1：目标是方法 2：运行时生效
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalInterceptor {

    // 校验登录 默认需要校验
    boolean checkLogin() default true;

    // 校验是否是管理员 默认不需要校验
    boolean checkAdmin() default false;

    // 校验登出 默认不需要校验
    boolean checkLogout() default false;
}
