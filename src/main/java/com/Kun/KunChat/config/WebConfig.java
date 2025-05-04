package com.Kun.KunChat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Author: Beta
 * Date: 2025/4/28 18:49
 * Description: 跨域了
 **/

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 添加映射路径
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173") // 允许哪些域的请求，星号代表允许所有
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE") // 允许的方法
                .allowedHeaders("*") // 允许的头部设置
                .allowCredentials(true) // 是否发送cookie
                .maxAge(168000); // 预检间隔时间
    }
}