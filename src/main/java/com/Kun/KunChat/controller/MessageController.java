package com.Kun.KunChat.controller;

import com.Kun.KunChat.common.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Beta
 * Date: 2025/4/26 13:34
 * Description: 预留站内信消息控制类
 **/

@RestController("messageController")
@Validated
@RequestMapping("/message")
public class MessageController extends BaseController {



}
