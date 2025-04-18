package com.Kun.KunChat.common;


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
    protected ResponseGlobal<Object> getFailedResponse(Integer code, String msg) {
        responseGlobal.setCode(code);
        responseGlobal.setMsg(msg);
        responseGlobal.setData(null);
        return responseGlobal;
    }

}
