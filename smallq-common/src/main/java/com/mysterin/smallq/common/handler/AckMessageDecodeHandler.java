package com.mysterin.smallq.common.handler;

import com.alibaba.fastjson.JSON;
import com.mysterin.smallq.common.msg.AckMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/13 20:45
 */
public class AckMessageDecodeHandler extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        AckMessage message = JSON.parseObject(bytes, AckMessage.class);
        out.add(message);
    }
}
