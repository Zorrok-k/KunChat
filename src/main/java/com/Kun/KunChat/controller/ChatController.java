package com.Kun.KunChat.controller;

import com.Kun.KunChat.annotation.GlobalInterceptor;
import com.Kun.KunChat.common.BaseController;
import com.Kun.KunChat.common.ResponseGlobal;
import com.Kun.KunChat.entity.UserChatInfo;
import com.Kun.KunChat.service.RedisService;
import com.Kun.KunChat.service.UserChatInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.Kun.KunChat.StaticVariable.RedisKeys.UNREAD;
import static com.Kun.KunChat.StaticVariable.Status.INFO_UNREADEMPTY;

/**
 * Author: Beta
 * Date: 2025/4/26 13:34
 * Description: 预留站内信消息控制类
 **/

@RestController("chatController")
@Validated
@RequestMapping("/chat")
public class ChatController extends BaseController {

    @Autowired
    private UserChatInfoService userChatInfoService;

    @Autowired
    private RedisService redisService;

    @GlobalInterceptor
    @RequestMapping("/unread")
    public ResponseGlobal<Object> getUnread() {
        String[] result = (String[]) RequestContextHolder.currentRequestAttributes().getAttribute("result", RequestAttributes.SCOPE_REQUEST);
        assert result != null;
        String userId = result[1];
        Set<String> unreadKeys = redisService.getKeysByPrefix(UNREAD + userId);
        Map<String, List<UserChatInfo>> unreadMap = new HashMap<>();
        for (String key : unreadKeys) {
            unreadMap.put(key.replace(UNREAD + userId + ":", ""), redisService.getValue(key));
        }
        // 初期为空加点测试数据
        if (unreadMap.isEmpty()) {
            return getSuccessResponse(INFO_UNREADEMPTY);
        }
        return getSuccessResponse(unreadMap);
    }

}
