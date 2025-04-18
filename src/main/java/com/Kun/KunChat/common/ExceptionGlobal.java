package com.Kun.KunChat.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Author: Beta
 * Date: 2025/4/18 15:15
 * Param:
 * Return:
 * Description:全局异常处理类
 **/

@Slf4j
@RestControllerAdvice
public class ExceptionGlobal extends BaseController {

    // 处理业务异常 代码和原因都是自定义的 犹豫不知道怎么设计，干脆 1000~2000
    @ExceptionHandler(value = BusinessException.class)
    public ResponseGlobal<Object> businessException(BusinessException e) {
        log.error("\n\n业务异常({}) => 原因: {}\n", e.getCode(), e.getMessage());
        return getFailedResponse(e.getCode(), e.getMessage());
    }

    // 处理空指针异常
    @ExceptionHandler(value = {NullPointerException.class})
    public ResponseGlobal<Object> globalException(NullPointerException e) {
        log.error("\n\n空指针异常({}) => 原因: {}\n", 1400, e.getMessage());
        return getFailedResponse(2000, "空指针异常！请检查传参。");
    }

    // 处理未知异常
    @ExceptionHandler(value = Exception.class)
    public ResponseGlobal<Object> globalException(Exception e) {
        log.error("\n\n未知异常({}) => 原因: {}\n",2000, e.getMessage());
        return getFailedResponse(2000, "服务器异常！请稍后尝试。");
    }

}
