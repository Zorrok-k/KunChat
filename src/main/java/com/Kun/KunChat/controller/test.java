package com.Kun.KunChat.controller;

import com.Kun.KunChat.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.Kun.KunChat.service.UserInfoService;

/**
 * Author: Beta
 * Date: 2025/4/17 15:39
 * Param:
 * Return:
 * Description:
 **/

@RestController
public class test {

    @Autowired
    UserInfoService userInfoService;

    @RequestMapping("/test")
    @ResponseBody
    public UserInfo getUser(){

        return userInfoService.getById("1");
    }
}
