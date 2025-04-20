package com.Kun.KunChat.controller;

import com.Kun.KunChat.common.BaseController;
import com.Kun.KunChat.common.BusinessException;
import com.Kun.KunChat.common.ResponseGlobal;
import com.Kun.KunChat.common.Status;
import com.Kun.KunChat.entity.UserInfo;
import com.Kun.KunChat.service.RedisService;
import com.Kun.KunChat.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
public class AccountController extends BaseController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("/checkCode")
    public ResponseGlobal<Object> checkCode() {
        // 图形验证码
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(100, 42);
        // 生成验证码和图片
        String code = captcha.text();
        String codeBase64 = captcha.toBase64();
        // 设置当前验证码唯一标识
        String codeSign = UUID.randomUUID().toString();
        // 验证码存入Redis
        redisService.putString("CodeSign:" + codeSign, code, 60 * 5);
        // 存入容器
        Map<String, String> data = new HashMap<>();
        data.put("codeSign", codeSign);
        data.put("codeBase64", codeBase64);
        return getSuccessResponse(data);
    }

    // 查询邮箱是否存在
    @RequestMapping("/checkEmail")
    public ResponseGlobal<Object> checkEmail(@NotEmpty @Email String email) {
        try {
            if (userInfoService.checkEmail(email) != null) {
                throw new BusinessException(Status.ERROR_EMAILEXITS);
            }
        } finally {

        }
        return getSuccessResponse();
    }

    @PostMapping("/register")
    public ResponseGlobal<Object> userRegister(@NotEmpty String nikeName,
                                               @NotEmpty @Email String email,
                                               @NotEmpty String password,
                                               @NotEmpty String code,
                                               @NotEmpty String codeSign) {
        // 这样写验证码尝试机会只有一次，不过重新获取一个也很快
        try {
            if (redisService.hasKey("CodeSign:" + codeSign)) {
                if (!code.equalsIgnoreCase(redisService.getString("CodeSign:" + codeSign))) {
                    throw new BusinessException(Status.ERROR_CHECKCODEWRONG);
                }
                if (userInfoService.checkEmail(email) != null) {
                    throw new BusinessException(Status.ERROR_EMAILEXITS);
                }
                UserInfo user = userInfoService.addUser(nikeName, email, password);
                if (user == null) {
                    throw new BusinessException(Status.FAILED);
                }
                return getSuccessResponse(user);
            } else {
                throw new BusinessException(Status.ERROR_CHECKCODELOSE);
            }

        } finally {
            redisService.deleteString("CodeSign:" + codeSign);
        }
    }

    // 测试自动注解
    @Cacheable(value = "{Test}", keyGenerator = "KeyGenerator", cacheManager = "CacheManager_User" )
    @RequestMapping("/test")
    public ResponseGlobal<Object> autoCache(@NotEmpty String id) {
        try {
            return getSuccessResponse(userInfoService.getUserById(id));
        } finally {

        }
    }

}
