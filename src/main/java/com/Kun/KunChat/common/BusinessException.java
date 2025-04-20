package com.Kun.KunChat.common;

import lombok.Getter;
/**
 * Author: Beta
 * Date: 2025/4/18 15:57
 * Param:
 * Return:
 * Description: 业务异常状态类
 **/
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 自定义业务异常代码
     */

    private final Integer code;

    private final String message;

    public BusinessException(Status status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

}
