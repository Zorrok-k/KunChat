package com.Kun.KunChat.netty;

import com.Kun.KunChat.common.ApplicationContextProvider;
import com.Kun.KunChat.config.NettyConfig;
import com.Kun.KunChat.entity.UserChatInfo;
import com.Kun.KunChat.service.GroupInfoService;
import com.Kun.KunChat.service.RedisService;
import com.Kun.KunChat.service.UserChatInfoService;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.Kun.KunChat.StaticVariable.RedisKeys.UNREAD;


/**
 * Author: Beta
 * Date: 2025/5/4 14:06
 * Description:
 **/

@Slf4j
@Component
public class WebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 客户端连接建立时触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("[服务器] 有客户端连接：{}，ip：{}", ctx.channel().id().asLongText(), ctx.channel().remoteAddress());

    }

    @Transactional
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        RedisService redisService = ApplicationContextProvider.getBean(RedisService.class);
        GroupInfoService groupInfoService = ApplicationContextProvider.getBean(GroupInfoService.class);
        UserChatInfoService userChatInfoService = ApplicationContextProvider.getBean(UserChatInfoService.class);
        if (msg.text().isEmpty() || msg.text() == null) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame("消息不合法"));
            return;
        }
        if (!NettyConfig.getChannelGroup().contains(ctx.channel())) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame("此连接未通过验证，驳回消息"));
            return;
        }
        if (msg.text().equalsIgnoreCase("heart beat")) {
            log.info("[服务器] 客户端心跳：{}", ctx.channel().attr(AttributeKey.valueOf("userId")).get());
            return;
        }
        UserChatInfo userChatInfo = JSON.parseObject(msg.text(), UserChatInfo.class);
        log.info("[服务器] 收到消息：{}，反序列化后：{}", msg.text(), userChatInfo);
        userChatInfo.setStatus(1);
        userChatInfo = userChatInfoService.addUserChatInfo(userChatInfo);
        if (userChatInfo == null) {
            return;
        }
        log.info("[服务器] 已将消息持久化：{}", userChatInfo);
        String senderId = userChatInfo.getSenderId();
        String receiverId = userChatInfo.getReceiverId();
        int chatType = userChatInfo.getChatType();

        // 统一接收者列表
        List<String> receivers = new ArrayList<>();

        if (chatType == 1) {
            // 群聊：获取成员列表
            receivers = groupInfoService.getGroupMembers(receiverId);
        } else {
            // 单聊：只加一个接收者
            receivers.add(receiverId);
        }

        // 遍历每个接收者
        for (String memberId : receivers) {
            if (memberId.equalsIgnoreCase(senderId)) {
                continue;// 排除自己
            }

            Channel channel = NettyConfig.getUserChannelMap().get(memberId);

            String chatKey;
            if (chatType == 1) {
                chatKey = UNREAD + memberId + ":" + receiverId; // 群聊 key
            } else {
                chatKey = UNREAD + memberId + ":" + senderId;   // 单聊 key
            }

            if (channel != null && channel.isActive()) {
                // 在线：直接发送
                channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(userChatInfo)));
                log.info("[服务器] 给用户 {} 发送消息", memberId);
            } else {
                // 不在线：添加离线消息
                List<UserChatInfo> unReadMessages = new ArrayList<>();
                if (redisService.hasKey(chatKey)) {
                    unReadMessages = redisService.getValue(chatKey);
                }
                unReadMessages.add(userChatInfo);
                if (unReadMessages.size() > 10) {
                    unReadMessages.remove(0);
                }
                redisService.setValue(chatKey, unReadMessages);
                log.info("[服务器] 添加用户 {} 的离线消息队列: {}", memberId, unReadMessages);
            }
        }
        // 给发送者回报消息发送成功 能走到这肯定不失败，因为我加了事务 👍
        ctx.channel().writeAndFlush(new TextWebSocketFrame("success"));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[服务器] WebSocketServerHandler异常: {}", ctx.channel(), cause);
        ctx.channel().close();
    }
}


