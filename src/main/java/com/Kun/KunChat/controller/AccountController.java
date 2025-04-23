package com.Kun.KunChat.controller;

import com.Kun.KunChat.annotation.GlobalInterceptor;
import com.Kun.KunChat.common.*;
import com.Kun.KunChat.entity.UserInfo;
import com.Kun.KunChat.service.RedisService;
import com.Kun.KunChat.service.UserInfoService;
import com.wf.captcha.ArithmeticCaptcha;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

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

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private CustomizeUtils customizeUtils;


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
            // 这里不抛异常是因为开启了事务，失败回滚，能跑到这里一定是成功的，报错再说，我没考虑
            UserInfo userInfo = userInfoService.login(email, password);
            // 生成在Redis中的唯一登录ID
            String loginId = customizeUtils.getUUID();
            // 用这个ID加密成Token，传回来解密才能找到是否登录
            String token = tokenUtils.createToken(loginId);
            redisService.setValue(RedisKeys.LOGINID.getKey() + loginId, userInfo.getUserId(), 60 * 60 * 24 * 7);
            return getSuccessResponse(token);

        } finally {
        }
    }

    @GlobalInterceptor
    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseGlobal<Object> userUpdate(@RequestBody @Validated(UserInfo.UpdateGroup.class) UserInfo userForm) {
        try {
            String userId = (String) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            userForm.setUserId(userId);
            // 查询邮箱是否重复
            if (userInfoService.checkEmail(userForm.getEmail()) != null) {
                throw new BusinessException(Status.ERROR_EMAILEXITS);
            }
            // 这里不抛异常是因为开启了事务，失败回滚，能跑到这里一定是成功的，报错再说，我没考虑
            return getSuccessResponse(userInfoService.updateUser(userForm));
        } finally {
        }
    }

    @GlobalInterceptor(checkLogin = false, checkLogout = true)
    @RequestMapping("/loginOut")
    public ResponseGlobal<Object> loginOut(@RequestHeader @NotEmpty String token) {
        try {
            /**
             * 解密token result[0] = loginId; result[1] = userId; type 1 是验证登录获取登录信息，2是登出
             * 异常在 verify() 里抛了，别傻不愣登怀疑自己！老忘记……
             */
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            // 删除用户凭证和登出 同样是开启了事务 加了个断言……看看先
            assert result != null;
            userInfoService.loginOut(result[0], result[1]);
            return getSuccessResponse();
        } finally {
        }
    }

    // 查询一个用户
    @RequestMapping("/test")
    public ResponseGlobal<Object> update(@RequestBody @Validated(UserInfo.UpdateGroup.class) UserInfo userForm) {
        try {
            UserInfo user = new UserInfo();
            user.setEmail(userForm.getEmail());
            // 查询邮箱和ID是否重复
            if (userInfoService.checkEmail(userForm.getEmail()) != null) {
                throw new BusinessException(Status.ERROR_EMAILEXITS);
            }
            // 这里不抛异常是因为开启了事务，失败回滚，能跑到这里一定是成功的，报错再说，我没考虑
            return getSuccessResponse(userInfoService.updateUser(userForm));
        } finally {
        }
    }

}
