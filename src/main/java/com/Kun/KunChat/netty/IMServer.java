package com.Kun.KunChat.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * Author: Beta
 * Date: 2025/5/4 13:30
 * Description: netty 服务配置类
 **/

@Component
public class IMServer {

    private static final Logger log = LoggerFactory.getLogger(IMServer.class);
    // 从配置文件中读取端口号，默认8888
    @Value("${netty.port}")
    private int port;

    // 主线程组，用于接收客户端连接
    private EventLoopGroup bossGroup;
    // 工作线程组，用于处理网络读写
    private EventLoopGroup workerGroup;

    /**
     * 启动 Netty 服务器
     *
     * @PostConstruct 是 Java EE 中定义的注解，Spring 也使用该注解来表示某个方法应该在 Bean 初始化后执行一次。
     * 该注解可以标注在一个没有参数的 非静态 方法上。Spring 会在容器初始化时自动调用这个方法。
     */
    @PostConstruct
    public void start() throws InterruptedException {
        // 创建主线程组，接收客户端连接 1个线程足够
        bossGroup = new NioEventLoopGroup(1);
        // 创建工作线程组，处理网络读写 默认CPU核心数*2
        workerGroup = new NioEventLoopGroup();

        // 创建服务器启动对象
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                // 使用NioServerSocketChannel作为服务器通道实现
                .channel(NioServerSocketChannel.class)
                // 设置服务器监听端口
                .localAddress(new InetSocketAddress(port))
                // 初始化通道，设置处理器
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        // 添加自定义处理器
                        ch.pipeline().addLast(new HttpServerCodec())
                                // 支持大数据流
                                .addLast(new ChunkedWriteHandler())
                                // 对http消息做聚合操作，会产生FullHttpRequest、FullHttpResponse
                                .addLast(new HttpObjectAggregator(1024 * 64))
                                // 用来鉴权的业务
                                .addLast(new authenticationHandler())
                                .addLast(new WebSocketServerProtocolHandler("/"))
                                .addLast(new WebSocketServerHandler());

                    }
                })
                // 服务器等待队列大小
                .option(ChannelOption.SO_BACKLOG, 128)
                // 保持长连接
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // 绑定端口启动服务器
        ChannelFuture future = bootstrap.bind().sync();
        if (future.isSuccess()) {
            IMServer.log.info("服务器连接池已启动，端口：{}", port);
        }
    }

    /**
     * 关闭 Netty服务器
     *
     * @PreDestroy 注解在 Spring Bean销毁前执行
     */
    @PreDestroy
    public void stop() {
        // 优雅关闭线程组
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        IMServer.log.info("服务器连接池已关闭");
    }
}
