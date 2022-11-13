package com.mysterin.smallq.server.handler;

import com.mysterin.smallq.common.msg.AckMessage;
import com.mysterin.smallq.common.msg.BaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/10 17:03
 */
@Slf4j
public class BaseMessageHandler extends SimpleChannelInboundHandler<BaseMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BaseMessage msg) throws Exception {
        log.debug("接收：{}", msg);
        AckMessage ackMessage = new AckMessage();
        ackMessage.setId(msg.getId());
        ackMessage.setTopic(msg.getTopic());
        ackMessage.setSuccess(true);
        ctx.channel().writeAndFlush(ackMessage);
    }
}
