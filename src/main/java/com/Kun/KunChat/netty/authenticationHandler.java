package com.Kun.KunChat.netty;

import com.Kun.KunChat.common.TokenUtils;
import com.Kun.KunChat.config.NettyConfig;
import com.Kun.KunChat.service.RedisService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.Kun.KunChat.StaticVariable.RedisKeys.LOGINID;


/**
 * Author: Beta
 * Date: 2025/5/5 9:00
 * Description: 继承 Channel 处理器并实现 channelRead 作为连接认证
 **/
@ChannelHandler.Sharable
@Component
@Slf4j

public class authenticationHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RedisService redisService;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest request) {
            // 获取请求头
            HttpHeaders headers = request.headers();
            if (headers.isEmpty()) {
                ctx.channel().close();
            }
            String token = headers.get("Sec-WebSocket-Protocol");
            String loginId = tokenUtils.parseToken(token);
            if (!redisService.hasKey(LOGINID + loginId)) {
                ctx.channel().close();
            }
            String userId = redisService.getValue(LOGINID + loginId).toString();
            log.info("[服务器] 获取到token: {}，登录ID为：{}---解析成功，用户ID为：{}", token, loginId, userId);
            // 将用户ID作为自定义属性加入到channel中，方便随时channel中获取用户ID
            ctx.channel().attr(AttributeKey.valueOf("userId")).setIfAbsent(userId);
            // 存进map
            NettyConfig.getUserChannelMap().put(userId, ctx.channel());
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
