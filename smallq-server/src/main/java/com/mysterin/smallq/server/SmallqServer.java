package com.mysterin.smallq.server;

import com.mysterin.smallq.server.config.SmallqConfig;
import com.mysterin.smallq.server.handler.SimpleHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/10 11:20
 */
@Slf4j
public class SmallqServer {

    private SmallqConfig smallqConfig;

    public static void main(String[] args) {
        SmallqConfig smallqConfig = new SmallqConfig();
        SmallqServer smallqServer = new SmallqServer(smallqConfig);
        smallqServer.start();
    }

    public SmallqServer(SmallqConfig smallqConfig) {
        this.smallqConfig = smallqConfig;
    }

    /**
     * 开启
     */
    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new SimpleHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(smallqConfig.getPort()).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("启动失败", e);
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
