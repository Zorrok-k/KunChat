package com.Kun.KunChat.aspect;

import com.Kun.KunChat.StaticVariable.Status;
import com.Kun.KunChat.annotation.GlobalInterceptor;
import com.Kun.KunChat.common.BusinessException;
import com.Kun.KunChat.common.TokenUtils;
import com.Kun.KunChat.service.GroupInfoService;
import com.Kun.KunChat.service.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Objects;

import static com.Kun.KunChat.StaticVariable.RedisKeys.LOGINID;

/**
 * Author: Beta
 * Date: 2025/4/23 11:17
 * Description: 拦截具体操作类
 **/

@Component("operationAspect")
@Aspect
public class GlobalOperationAspect {

    private static final Logger log = LoggerFactory.getLogger(GlobalOperationAspect.class);
    @Autowired
    private RedisService redisService;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private TokenUtils tokenUtils;

    @Before("@annotation(com.Kun.KunChat.annotation.GlobalInterceptor)")
    public void interceptorDo(JoinPoint point) {
        try {
            // 这是在确认添加注解的方法上面是否是这个注解
            Method method = ((MethodSignature) point.getSignature()).getMethod();
            GlobalInterceptor interceptor = method.getAnnotation(GlobalInterceptor.class);
            if (null == interceptor) {
                return;
            }
            // 获取全局的请求
            HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
            // 校验登录
            if (interceptor.checkLogin() || interceptor.checkAdmin()) {
                checkLogin(request, interceptor.checkAdmin());
            }
            // 校验登出
            if (interceptor.checkLogout()) {
                checkLoginOut(request);
            }
        } finally {
        }
    }

    private void checkLogin(HttpServletRequest request, boolean admin) {
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            throw new BusinessException(Status.ERROR_NOTLOGIN);
        }
        // 解密token获取登录凭证和用户id
        String loginId = tokenUtils.parseToken(token);
        if (!redisService.hasKey(LOGINID + loginId)) {
            throw new BusinessException(Status.ERROR_LOGINLOSE);
        }
        String userId = redisService.getValue(LOGINID + loginId).toString();
        // 给被拦截方法传递数据 result[0] = loginId; result[1] = userId
        Objects.requireNonNull(RequestContextHolder.getRequestAttributes()).setAttribute("result", new String[]{loginId, userId}, RequestAttributes.SCOPE_REQUEST);
        if (admin && !userId.equals("Admin")) {
            throw new BusinessException(Status.ERROR_ADMIN);
        }
    }

    private void checkLoginOut(HttpServletRequest request) {
        // 强制退出
        if (redisService.hasKey("CheckLoginOut::" + request.getParameter("email"))) {
            String loginId = redisService.getValue("CheckLoginOut::" + request.getParameter("email")).toString();
            if (redisService.hasKey(LOGINID + loginId)) {
                redisService.delete(LOGINID + loginId);
            }
        }
    }

}
