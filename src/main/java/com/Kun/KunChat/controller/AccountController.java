package com.Kun.KunChat.controller;

import com.Kun.KunChat.common.*;
import com.Kun.KunChat.entity.UserInfo;
import com.Kun.KunChat.service.RedisService;
import com.Kun.KunChat.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
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

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);
    @Autowired
    private RedisService redisService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TokenUtils tokenUtils;

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
        redisService.setValue(RedisKeys.CODESIGN.getKey() + codeSign, code, 60 * 5);
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
    public ResponseGlobal<Object> userRegister(@NotEmpty String nikeName, @NotEmpty @Email String email, @NotEmpty String password, @NotEmpty String code, @NotEmpty String codeSign) {
        // 这样写验证码尝试机会只有一次，不过重新获取一个也很快
        try {
            if (redisService.hasKey(RedisKeys.CODESIGN.getKey() + codeSign)) {
                if (!code.equalsIgnoreCase(redisService.getValue(RedisKeys.CODESIGN.getKey() + codeSign).toString())) {
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
            redisService.delete(RedisKeys.CODESIGN.getKey() + codeSign);
        }
    }

    @RequestMapping("/login")
    public ResponseGlobal<Object> userLogin(@NotEmpty @Email String email, @NotEmpty String password) {
        try {
            UserInfo userInfo = userInfoService.login(email, password);
            if (userInfo == null) {
                throw new BusinessException(Status.ERROR_LOGIN);
            }
            // 生成在Redis中的唯一登录ID
            String loginId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            // 用这个ID加密成Token，传回来解密才能找到是否登录
            String token = tokenUtils.createToken(loginId);
            redisService.setValue(RedisKeys.LOGINID.getKey() + loginId, userInfo.getUserId(), 60 * 60 * 24 * 7);
            return getSuccessResponse(token);
        } finally {
        }
    }

    @RequestMapping("/loginVerify")
    public ResponseGlobal<Object> loginVerify(@RequestHeader String token) {
        try {
            // 解密token获取登录凭证
            String loginId = tokenUtils.parseToken(token);
            if (!redisService.hasKey(RedisKeys.LOGINID.getKey() + loginId)) {
                throw new BusinessException(Status.ERROR_LOGINLOSE);
            }
            String userId = redisService.getValue(RedisKeys.LOGINID.getKey() + loginId).toString();
            // 返回用户信息
            UserInfo userInfo = userInfoService.getUserById(userId);
            return getSuccessResponse(userInfo);
        } finally {
        }
    }

    @RequestMapping("/loginOut")
    public ResponseGlobal<Object> loginOut(@RequestHeader String token) {
        try {
            // 解密token获取登录凭证
            String loginId = tokenUtils.parseToken(token);
            if (!redisService.hasKey(RedisKeys.LOGINID.getKey() + loginId)) {
                throw new BusinessException(Status.ERROR_LOGINOUT);
            }
            String userId = redisService.getValue(RedisKeys.LOGINID.getKey() + loginId).toString();
            // 删除用户凭证登出
            userInfoService.loginOut(loginId, userId);
            return getSuccessResponse();
        } finally {
        }
    }

    // 查询一个用户
    @RequestMapping("/test")
    public ResponseGlobal<Object> getById(@NotEmpty String id) {
        try {
            return getSuccessResponse(userInfoService.getUserById(id));
        } finally {

        }
    }

}
