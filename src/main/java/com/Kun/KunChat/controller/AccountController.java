package com.Kun.KunChat.controller;

import com.Kun.KunChat.common.ResponseGlobal;
import com.Kun.KunChat.service.RedisService;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Author: Beta
 * Date: 2025/4/17 20:30
 * Param:
 * Return:
 * Description:用户账户控制类
 **/

@RestController("accountController")
@RequestMapping("/account")
@Validated
public class AccountController extends BaseController{

    @Autowired
    private RedisService redisService;

    @RequestMapping("/checkCode")
    public ResponseGlobal<Object> checkCode() {
        // 图形验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100,42);
        // 生成验证码和图片
        String code = captcha.text();
        String codeBase64 = captcha.toBase64();
        // 验证码存入Redis
        redisService.putString("KunChat_CheckCode:",code);
        redisService.setTime("KunChat_CheckCode:",60*5);
        // 设置当前验证码唯一标识
        String codeSign = UUID.randomUUID().toString();
        // 存入容器
        Map<String,String> data = new HashMap<>();
        data.put("codeSign",codeSign);
        data.put("codeBase64",codeBase64);
        return getSuccessResponse(data);
    }


}
