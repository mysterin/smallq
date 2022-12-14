package com.mysterin.smallq.client;

import com.alibaba.fastjson.JSON;
import com.mysterin.smallq.common.handler.AckMessageDecodeHandler;
import com.mysterin.smallq.common.handler.BaseMessageEncodeHandler;
import com.mysterin.smallq.common.msg.AckMessage;
import com.mysterin.smallq.common.msg.BaseMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/10 17:29
 */
@Slf4j
public class SmallqClient extends ChannelInboundHandlerAdapter {

    private String host;
    private Integer port;

    private Channel channel;
    private final Map<Long, AckMessage> result = new ConcurrentHashMap<>();

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
                            ch.pipeline()
                                    .addLast(new BaseMessageEncodeHandler())
                                    .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 2, 0, 2))
                                    .addLast(new AckMessageDecodeHandler())
                                    .addLast(SmallqClient.this);
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            if (channelFuture.isSuccess()) {
                log.info("????????????: {}:{}", host, port);
                channel = channelFuture.channel();
            } else {
                throw new RuntimeException("????????????");
            }
        } catch (Exception e) {
            log.error("????????????", e);
            eventLoopGroup.shutdownGracefully();
        }
    }

    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        AckMessage ackMessage = (AckMessage) msg;
        result.put(ackMessage.getId(), ackMessage);
        notifyAll();
    }

    /**
     * ????????????
     * @param message
     * @return
     */
    public synchronized AckMessage send(BaseMessage message) {
        try {
            log.debug("?????????{}", JSON.toJSONString(message));
            channel.writeAndFlush(message);
            long start = System.currentTimeMillis();
            while ((System.currentTimeMillis() - start) < 3000) {
                wait(3000L);
                Long id = message.getId();
                AckMessage ackMessage = result.get(id);
                if (ackMessage != null) {
                    result.remove(id);
                    log.debug("?????????{}", JSON.toJSONString(ackMessage));
                    return ackMessage;
                }
            }
            log.error("??????????????????, message={}", message);
        } catch (Exception e) {
            log.error("????????????????????? msg={}", message, e);
        }
        throw new RuntimeException("????????????, message=" + message);
    }
}
