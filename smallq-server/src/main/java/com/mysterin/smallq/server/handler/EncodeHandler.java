package com.mysterin.smallq.server.handler;

import com.alibaba.fastjson.JSON;
import com.mysterin.smallq.common.msg.BaseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/12 22:51
 */
public class EncodeHandler extends MessageToByteEncoder<BaseMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, BaseMessage msg, ByteBuf out) throws Exception {
        byte[] bytes = JSON.toJSONBytes(msg);
        out.writeBytes(bytes);
    }
}
