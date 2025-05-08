package com.Kun.KunChat.config;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Author: Beta
 * Date: 2025/5/5 8:35
 * Description: 管理所有 Channel 的单例对象
 **/
@Component
@Getter
public class NettyConfig {

    // 定义一个channel组，管理所有的channel GlobalEventExecutor.INSTANCE 是全局的事件执行器，是一个单例
    private static final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 存放用户与Chanel的对应信息，用于给指定用户发送消息
    private static final ConcurrentHashMap<String, Channel> userChannelMap = new ConcurrentHashMap<>();

    // 私有化构建函数
    private NettyConfig() {
    }

    public static ChannelGroup getChannelGroup() {
        return channelGroup;
    }

    public static ConcurrentHashMap<String, Channel> getUserChannelMap() {
        return userChannelMap;
    }
}
