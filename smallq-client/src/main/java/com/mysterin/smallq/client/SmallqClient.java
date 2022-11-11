package com.mysterin.smallq.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/10 17:29
 */
@Slf4j
public class SmallqClient extends ChannelInboundHandlerAdapter {

    private String host;
    private Integer port;

    private ChannelHandlerContext ctx;

    public static void main(String[] args) throws InterruptedException {
        SmallqClient smallqClient = new SmallqClient("127.0.0.1", 22333);
        for (int i=0; i<5; i++) {
            smallqClient.sendMsg("hello"+i);
            Thread.sleep(1000);
        }
    }

    public SmallqClient(String host, Integer port) {
        this.host = host;
        this.port = port;
        initSocket();
    }

    public void initSocket() {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(SmallqClient.this);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            if (channelFuture.isSuccess()) {
                log.info("连接成功: {}:{}", host, port);
            } else {
                throw new RuntimeException("连接失败");
            }
        } catch (Exception e) {
            log.error("连接失败", e);
            eventLoopGroup.shutdownGracefully();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("active", CharsetUtil.UTF_8));
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        log.debug("接收：{}", byteBuf.toString(CharsetUtil.UTF_8));
    }

    public void sendMsg(String msg) {
        log.debug("推送：{}", msg);
        ctx.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    }
}
