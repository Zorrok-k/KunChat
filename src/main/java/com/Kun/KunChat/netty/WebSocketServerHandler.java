package com.Kun.KunChat.netty;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private UserChatInfoService userChatInfoService;

    @Autowired
    private GroupInfoService groupInfoService;

    @Autowired
    private RedisService redisService;

    // 客户端连接建立时触发
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("[服务器] 有客户端连接：{}，ip：{}", ctx.channel().id().asLongText(), ctx.channel().remoteAddress());
        // 添加到channelGroup 通道组
        NettyConfig.getChannelGroup().add(ctx.channel());
    }

    @Transactional
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        if (msg.text().isEmpty() || msg.text() == null) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame("消息不合法"));
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
        // 给目标发消息
        Channel receiverCh = NettyConfig.getUserChannelMap().get(userChatInfo.getReceiverId());
        // 先判断是不是群组消息 是群组要遍历然后再看每个人在不在线
        if (userChatInfo.getChatType() == 1) {
            // 拿到该群组成员列表
            List<String> groupMembers = groupInfoService.getGroupMembers(userChatInfo.getReceiverId());
            for (String groupMember : groupMembers) {
                // 如果在线 直接发
                if (NettyConfig.getUserChannelMap().containsKey(groupMember)) {
                    NettyConfig.getUserChannelMap().get(groupMember).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(userChatInfo)));
                    log.info("[服务器] 给群组成员：{} 发送消息", groupMember);
                } else {
                    // 离线 给他创建一个未读消息的对象数组，这个是群组的未读消息对象数组
                    List<UserChatInfo> unReadGroupMessage = new ArrayList<>();
                    if (!redisService.hasKey(UNREAD + groupMember + ":" + userChatInfo.getReceiverId())) {
                        unReadGroupMessage.add(userChatInfo);
                    } else {
                        unReadGroupMessage = redisService.getValue(UNREAD + groupMember + ":" + userChatInfo.getReceiverId());
                        unReadGroupMessage.add(userChatInfo);
                        if (unReadGroupMessage.size() > 10) {
                            unReadGroupMessage.remove(0);
                        }
                    }
                    log.info("[服务器] 添加用户：{} 群组 {} 的离线消息队列：{}", groupMember, userChatInfo.getReceiverId(), unReadGroupMessage);
                    redisService.setValue(UNREAD + groupMember + ":" + userChatInfo.getReceiverId(), unReadGroupMessage);
                }
            }
        } else {
            if (NettyConfig.getUserChannelMap().containsKey(userChatInfo.getReceiverId())) {
                // 如果在线直接发
                NettyConfig.getUserChannelMap().get(userChatInfo.getReceiverId()).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(userChatInfo)));
                log.info("[服务器] 给：{} 发送消息", userChatInfo.getReceiverId());
            } else {
                // 给他创建一个未读消息的对象数组
                List<UserChatInfo> unReadUserMessage = new ArrayList<>();
                if (!redisService.hasKey(UNREAD + userChatInfo.getReceiverId() + ":" + userChatInfo.getSenderId())) {
                    unReadUserMessage.add(userChatInfo);
                } else {
                    unReadUserMessage = redisService.getValue(UNREAD + userChatInfo.getReceiverId() + ":" + userChatInfo.getSenderId());
                    unReadUserMessage.add(userChatInfo);
                    if (unReadUserMessage.size() > 10) {
                        unReadUserMessage.remove(0);
                    }
                }
                log.info("[服务器] 添加用户：{} 与 用户：{}的离线消息队列：{}", userChatInfo.getReceiverId(), userChatInfo.getSenderId(), unReadUserMessage);
                redisService.setValue(UNREAD + userChatInfo.getReceiverId() + ":" + userChatInfo.getSenderId(), unReadUserMessage);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[服务器] WebSocketServerHandler异常: {}", ctx.channel(), cause);
        ctx.channel().close();
    }
}


