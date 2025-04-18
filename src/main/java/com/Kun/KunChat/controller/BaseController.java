package com.Kun.KunChat.controller;


import com.Kun.KunChat.common.ResponseGlobal;

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
     *
     * 一般错误 500
     */
    private final ResponseGlobal<Object> responseGlobal = new ResponseGlobal<>();

    protected ResponseGlobal<Object> getSuccessResponse(Object data) {
        responseGlobal.setCode(200);
        responseGlobal.setMsg("succeed");
        responseGlobal.setData(data);
        return responseGlobal;
    }

    protected ResponseGlobal<Object> getSuccessResponse() {
        responseGlobal.setCode(200);
        responseGlobal.setMsg("succeed");
        responseGlobal.setData(null);
        return responseGlobal;
    }

    // 失败需要编写错误内容
    protected ResponseGlobal<Object> getFailedResponse(String msg) {
        responseGlobal.setCode(500);
        responseGlobal.setMsg(msg);
        responseGlobal.setData(null);
        return responseGlobal;
    }

    protected ResponseGlobal<Object> getFailedResponse() {
        responseGlobal.setCode(500);
        responseGlobal.setMsg("failed");
        responseGlobal.setData(null);
        return responseGlobal;
    }

}
