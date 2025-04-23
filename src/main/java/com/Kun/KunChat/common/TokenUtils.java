package com.Kun.KunChat.common;

import com.Kun.KunChat.service.RedisService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;


/**
 * Author: Beta
 * Date: 2025/4/20 15:48
 * Param:
 * Return:
 * Description: 生成和解析token
 **/

@Component
public class TokenUtils {

    @Autowired
    private RedisService redisService;

    private static final SecretKeySpec TOKEN_KEY = new SecretKeySpec("79EE9CF52F91D492ACB89FB6F40E0981".getBytes(), SignatureAlgorithm.HS256.getJcaName());

    public String createToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)  // 设置主题
                .setIssuedAt(new Date())  // 设置签发时间
                .setExpiration(new Date(System.currentTimeMillis() + 2592000000L))  // 设置过期时间
                .signWith(TOKEN_KEY)  // 使用密钥签名
                .compact();  // 生成JWT字符串
    }

    public String parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(TOKEN_KEY)  // 设置签名密钥
                .build()
                .parseClaimsJws(token)  // 解析token
                .getBody()  // 获取负载
                .getSubject();  // 获取主题(用户名)

    }

    // 解密token result[0] = loginId; result[1] = userId; type 1 是验证登录获取登录信息，2是登出
    public String[] verify(String token, int type) {
        // 解密token获取登录凭证和用户id
        String loginId = parseToken(token);
        if (!redisService.hasKey(RedisKeys.LOGINID.getKey() + loginId)) {
            // 想抛出不同的异常
            if (type == 1) {
                throw new BusinessException(Status.ERROR_LOGINLOSE);
            } else {
                throw new BusinessException(Status.ERROR_LOGINOUT);
            }
        }
        String userId = redisService.getValue(RedisKeys.LOGINID.getKey() + loginId).toString();
        return new String[]{loginId, userId};
    }


}
