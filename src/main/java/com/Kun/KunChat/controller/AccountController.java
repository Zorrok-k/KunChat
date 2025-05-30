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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.Kun.KunChat.StaticVariable.RedisKeys.CODESIGN;
import static com.Kun.KunChat.StaticVariable.RedisKeys.LOGINID;
import static com.Kun.KunChat.StaticVariable.Status.*;

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
        redisService.setValue(CODESIGN + codeSign, code, 60 * 5);
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
                throw new BusinessException(ERROR_EMAILEXITS);
            }
        } finally {

        }
        return getSuccessResponse();
    }

    @PostMapping("/register")
    public ResponseGlobal<Object> userRegister(@NotEmpty String nikeName, @NotEmpty @Email String email, @NotEmpty String password, @NotEmpty String code, @NotEmpty String codeSign) {
        // 这样写验证码尝试机会只有一次，不过重新获取一个也很快
        try {
            if (redisService.hasKey(CODESIGN + codeSign)) {
                if (!code.equalsIgnoreCase(redisService.getValue(CODESIGN + codeSign).toString())) {
                    throw new BusinessException(ERROR_CHECKCODEWRONG);
                }
                if (userInfoService.checkEmail(email) != null) {
                    throw new BusinessException(ERROR_EMAILEXITS);
                }
                UserInfo user = userInfoService.addUser(nikeName, email, password);
                if (user == null) {
                    throw new BusinessException(FAILED);
                }
                return getSuccessResponse(user);
            } else {
                throw new BusinessException(ERROR_CHECKCODELOSE);
            }

        } finally {
            redisService.delete(CODESIGN + codeSign);
        }
    }

    @GlobalInterceptor(checkLogin = false, checkLogout = true)
    @RequestMapping("/login")
    public ResponseGlobal<Object> userLogin(@NotEmpty @Email String email, @NotEmpty String password) {
        try {
            // 这里不抛异常是因为开启了事务，失败回滚，能跑到这里一定是成功的，报错再说，我没考虑
            UserInfo userInfo = userInfoService.login(email, password);
            // 如果为空 说明账号或密码错误 抛出异常
            if (userInfo == null) {
                throw new BusinessException(ERROR_LOGIN);
            }
            // 生成在Redis中的唯一登录ID
            String loginId = customizeUtils.getUUID();
            // 用这个ID加密成Token，传回来解密才能找到是否登录
            String token = tokenUtils.createToken(loginId);
            redisService.setValue(LOGINID + loginId, userInfo.getUserId(), 60 * 60 * 24 * 7);
            /**
             * 有点骑虎难下的感觉。本来这个token解密之后才能拿到登录信息，现在又非要公开一个key去拿登录信息的key，等于解密是一个摆设
             *
             * 但是没办法，不这样公开我在另一个没有token的设备就找不到登录信息，就没办法下线
             */
            redisService.setValue("CheckLoginOut::" + email, loginId);
            return getSuccessResponse(token);
        } finally {
        }
    }

    @GlobalInterceptor
    @PostMapping(value = "/update", consumes = "application/json")
    public ResponseGlobal<Object> userUpdate(@RequestBody @Validated(UserInfo.UpdateGroup.class) UserInfo userForm
    ) {
        try {
            /**
             * 解密token result[0] = loginId; result[1] = userId
             */
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            assert result != null;
            userForm.setUserId(result[1]);
            // 查询邮箱是否重复
            if (userInfoService.checkEmail(userForm.getEmail()) != null) {
                throw new BusinessException(ERROR_EMAILEXITS);
            }
            // 这里不抛异常是因为开启了事务，失败回滚，能跑到这里一定是成功的，报错再说，我没考虑
            return getSuccessResponse(userInfoService.updateUser(userForm));
        } finally {
        }
    }

    // 上传图片：头像、背景  总是报错搞不定只能另外创建一个接口了
    @GlobalInterceptor
    @PostMapping("/upLoad")
    public ResponseGlobal<Object> upLoad(@RequestParam MultipartFile avatar,
                                         @RequestParam MultipartFile userInfoCover) {
        try {
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            assert result != null;
            // 0是传头像 1是传背景图
            if (avatar != null && !avatar.isEmpty()) {
                customizeUtils.upLoad(avatar, result[1], 0);
            }
            if (userInfoCover != null && !userInfoCover.isEmpty()) {
                customizeUtils.upLoad(userInfoCover, result[1], 1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return getSuccessResponse();
    }


    @GlobalInterceptor
    @RequestMapping("/loginOut")
    public ResponseGlobal<Object> loginOut(@RequestHeader @NotEmpty String token) {
        try {
            /**
             * 解密token result[0] = loginId; result[1] = userId
             */
            String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
            // 删除用户凭证和登出 同样是开启了事务 加了个断言……看看先
            assert result != null;
            userInfoService.loginOut(result[0], result[1]);
            redisService.delete("CheckLoginOut::" + userInfoService.getUser(result[1]).getEmail());
            return getSuccessResponse();
        } finally {
        }
    }

    @RequestMapping("/search")
    public ResponseGlobal<Object> serach(@RequestParam(required = false) String userId,
                                         @RequestParam(required = false) String nickName,
                                         @RequestParam(required = false, defaultValue = "1") Integer page) {
        try {
            if (!userId.isEmpty()) {
                UserInfo userInfo = userInfoService.getUser(userId);
                if (userInfo == null) {
                    throw new BusinessException(ERROR_SERACH);
                }
                return getSuccessResponse(userInfo);
            } else {
                return getSuccessResponse(userInfoService.getUser(nickName, page));
            }
        } finally {
        }
    }


    @RequestMapping("/getInfo")
    public ResponseGlobal<Object> getUserId(@RequestParam @NotEmpty String userId) {
        return getSuccessResponse(userInfoService.getUser(userId));
    }
}
