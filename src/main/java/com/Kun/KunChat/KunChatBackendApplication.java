package com.Kun.KunChat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.Kun.KunChat"})
public class KunChatBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(KunChatBackendApplication.class, args);
        System.out.println("----------------------------------------------------------------------------------------------------------------\n");
        System.out.println("                                 Application：KunChat 服务端 正在运行！\n                                         ");
        System.out.println("                                 Test Url：http://localhost:8680/api/\n                                         ");
        System.out.println("----------------------------------------------------------------------------------------------------------------");
    }


}
