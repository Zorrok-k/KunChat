package com.Kun.KunChat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"com.Kun.KunChat"})
@MapperScan("com.Kun.KunChat.mapper")
public class KunChatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KunChatBackendApplication.class, args);
    }


}
