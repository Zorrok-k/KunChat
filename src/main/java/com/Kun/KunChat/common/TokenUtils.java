package com.Kun.KunChat.common;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;


/**
 * Author: Beta
 * Date: 2025/4/20 15:48
 * Param:
 * Return:
 * Description: 生成和解析token
 **/

@Component
public class TokenUtils {

    private static SecretKeySpec TOKEN_KEY;

    TokenUtils() {
    }

    @Value("${token.secret}")
    public void init(String secret) {
        TOKEN_KEY = new SecretKeySpec(secret.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

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
}
