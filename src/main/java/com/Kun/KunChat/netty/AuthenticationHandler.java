package com.Kun.KunChat.netty;

import com.Kun.KunChat.common.ApplicationContextProvider;
import com.Kun.KunChat.common.TokenUtils;
import com.Kun.KunChat.config.NettyConfig;
import com.Kun.KunChat.entity.UserInfo;
import com.Kun.KunChat.service.RedisService;
import com.Kun.KunChat.service.UserInfoService;
import com.alibaba.fastjson2.JSON;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.Kun.KunChat.StaticVariable.RedisKeys.LOGINID;


/**
 * Author: Beta
 * Date: 2025/5/5 9:00
 * Description: 继承 Channel 处理器并实现 channelRead 作为连接认证
 **/
@ChannelHandler.Sharable
@Slf4j
@Component
public class AuthenticationHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        TokenUtils tokenUtils = ApplicationContextProvider.getBean(TokenUtils.class);
        RedisService redisService = ApplicationContextProvider.getBean(RedisService.class);
        UserInfoService userInfoService = ApplicationContextProvider.getBean(UserInfoService.class);
        if (!(msg.text().isEmpty() || msg.text() == null)) {
            // log.info(msg.text());
            String loginId = tokenUtils.parseToken(msg.text());
            if (!redisService.hasKey(LOGINID + loginId)) {
                ctx.channel().close();
            }
            String userId = redisService.getValue(LOGINID + loginId).toString();
            log.info("[服务器] 获取到token: {}，登录ID为：{}---解析成功，用户ID为：{}", msg.text(), loginId, userId);
            // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户ID
            ctx.channel().attr(AttributeKey.valueOf("userId")).setIfAbsent(userId);
            // 添加到channelGroup 通道组 在通道组的都是验证通过的了
            NettyConfig.getChannelGroup().add(ctx.channel());
            // 存进map
            NettyConfig.getUserChannelMap().put(userId, ctx.channel());
            // 给客户端返回用户信息
            UserInfo userInfo = userInfoService.getUser(userId);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(userInfo)));
            log.info("[服务器] 发送用户信息：{}", userInfo);
            // 关闭这handler 这个客户端不会再走这个方法了
            ctx.pipeline().remove(this);
        } else {
            ctx.channel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("[服务器] authenticationHandler异常: {}", ctx.channel(), cause);
        ctx.channel().close();
    }
}
