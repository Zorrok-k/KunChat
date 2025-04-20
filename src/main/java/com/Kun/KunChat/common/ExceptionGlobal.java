package com.Kun.KunChat.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    // 处理业务异常 代码规范在Status枚举类
    @ExceptionHandler(value = BusinessException.class)
    public ResponseGlobal<Object> businessException(BusinessException e) {
        log.error("\n\n业务异常({}) => 原因: {}\n", e.getCode(), e.getMessage());
        return getFailedResponse(e.getCode(), e.getMessage());
    }

    // 处理 @Validated 校验异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseGlobal<Object> globalException(MethodArgumentNotValidException e) {
        log.error("\n\n参数不合法({}) => 原因: {}\n", Status.ERROR_VALIDATED.getCode(), e.getMessage());
        return getFailedResponse(Status.ERROR_VALIDATED);
    }

    // 处理空指针异常
    @ExceptionHandler(value = NullPointerException.class)
    public ResponseGlobal<Object> globalException(NullPointerException e) {
        log.error("\n\n空指针异常({}) => 原因: {}\n", Status.ERROR_NULLPOINTER.getCode(), e.getMessage());
        return getFailedResponse(Status.ERROR_NULLPOINTER);
    }

    // 处理未知异常
    @ExceptionHandler(value = Exception.class)
    public ResponseGlobal<Object> globalException(Exception e) {
        log.error("\n\n未知异常({}) => 原因: {}\n", Status.FAILED.getCode(), e.getMessage());
        return getFailedResponse(Status.FAILED);
    }

}
