package com.Kun.KunChat.common;


import com.Kun.KunChat.StaticVariable.Status;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Author: Beta
 * Date: 2025/4/17 22:16
 * Param:
 * Return:
 * Description:全局响应基础控制类
 **/

public class BaseController {

    /**
     * 成功 200
     * <p>
     * 一般错误 500
     */
    @Autowired
    private final ResponseGlobal<Object> responseGlobal;

    public BaseController(ResponseGlobal<Object> responseGlobal) {
        this.responseGlobal = responseGlobal;
    }


    // 无数据返回成功
    protected <T> ResponseGlobal<Object> getSuccessResponse() {
        responseGlobal.setCode(Status.SUCCEED.getCode());
        responseGlobal.setMsg(Status.SUCCEED.getMessage());
        responseGlobal.setData(null);
        return responseGlobal;
    }

    // 带数据返回成功
    protected <T> ResponseGlobal<Object> getSuccessResponse(T data) {
        responseGlobal.setCode(Status.SUCCEED.getCode());
        responseGlobal.setMsg(Status.SUCCEED.getMessage());
        responseGlobal.setData(data);
        return responseGlobal;
    }

    // 返回分页数据
    protected <T> ResponseGlobal<Object> getSuccessResponse(Page<T> thePage) {
        responseGlobal.setCode(Status.SUCCEED.getCode());
        responseGlobal.setMsg(Status.SUCCEED.getMessage());
        responseGlobal.setPage(thePage.getPages());
        responseGlobal.setTotal(thePage.getTotal());
        responseGlobal.setSize(thePage.getSize());
        responseGlobal.setData(thePage.getRecords());
        return responseGlobal;
    }

    // 抛出业务异常时
    protected <T> ResponseGlobal<Object> getFailedResponse(Integer code, String msg) {
        responseGlobal.setCode(code);
        responseGlobal.setMsg(msg);
        responseGlobal.setData(null);
        return responseGlobal;
    }

    protected <T> ResponseGlobal<Object> getFailedResponse(Status status) {
        responseGlobal.setCode(status.getCode());
        responseGlobal.setMsg(status.getMessage());
        responseGlobal.setData(null);
        return responseGlobal;
    }

}
