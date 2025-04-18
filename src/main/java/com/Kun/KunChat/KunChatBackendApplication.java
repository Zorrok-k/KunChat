package com.Kun.KunChat;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.Kun.KunChat"})
@MapperScan("com.Kun.KunChat.mapper")
public class KunChatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KunChatBackendApplication.class, args);
        System.out.println("------------------------------------------------------------------------------------");
        System.out.println("                   Application：KunChat 服务端 正在运行！                             ");
        System.out.println("                   Test Url：http://localhost:8680/api/                             ");
        System.out.println("------------------------------------------------------------------------------------");
    }


}
