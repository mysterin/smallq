package com.mysterin.smallq.server.handler;

import com.mysterin.smallq.common.msg.AckMessage;
import com.mysterin.smallq.common.msg.Action;
import com.mysterin.smallq.common.msg.BaseMessage;
import com.mysterin.smallq.common.msg.SmallqMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/16 10:11
 */
@Slf4j
public class SmallqMessageHandler extends SimpleChannelInboundHandler<SmallqMessage> {

    private Map<String, Queue<SmallqMessage>> queueMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SmallqMessage msg) throws Exception {
        log.debug("接收：{}", msg);
        Action action = msg.getAction();
        switch (action) {
            case PUT:
                putHandle(ctx, msg);
                break;
            case GET:
                getHandle(ctx, msg);
                break;
            default:
                break;
        }

    }

    private void putHandle(ChannelHandlerContext ctx, SmallqMessage msg) {
        AckMessage ackMessage = new AckMessage();
        ackMessage.setId(msg.getId());
        ackMessage.setTopic(msg.getTopic());
        ackMessage.setSuccess(true);
        ctx.channel().writeAndFlush(ackMessage);
    }

    private void getHandle(ChannelHandlerContext ctx, SmallqMessage msg) {

    }
}
