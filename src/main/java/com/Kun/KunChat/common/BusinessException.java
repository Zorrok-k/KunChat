package com.Kun.KunChat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Author: Beta
 * Date: 2025/4/18 15:57
 * Param:
 * Return:
 * Description:
 **/
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException{

    /**
     * 自定义业务异常代码
     */

    private Integer code;

    private String message;

}
