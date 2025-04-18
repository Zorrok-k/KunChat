package com.Kun.KunChat.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Author: Beta
 * Date: 2025/4/17 22:04
 * Param:
 * Return:
 * Description:全局状态响应类
 **/

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseGlobal<T> implements Serializable {

    // 状态码
    private Integer code;

    // 响应消息
    private String msg;

    // 响应数据
    private T data;

}
