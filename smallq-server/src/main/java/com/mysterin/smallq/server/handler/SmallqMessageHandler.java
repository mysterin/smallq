package com.mysterin.smallq.server.handler;

import com.mysterin.smallq.common.msg.AckMessage;
import com.mysterin.smallq.common.msg.MsgType;
import com.mysterin.smallq.common.msg.SmallqMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 处理消息
 * @author linxiaobin
 * @Description
 * @date 2022/11/16 10:11
 */
@Slf4j
public class SmallqMessageHandler extends SimpleChannelInboundHandler<SmallqMessage> {

    /**
     * 默认话题
     */
    private static final String DEFAULT_TOPIC = "DEFAULT_TOPIC";
    private Map<String, Queue<SmallqMessage>> queueMap = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SmallqMessage msg) throws Exception {
        log.debug("接收：{}", msg);
        MsgType msgType = msg.getMsgType();
        switch (msgType) {
            case PUT:
                handlePut(ctx, msg);
                break;
            case GET:
                handleGet(ctx, msg);
                break;
            default:
                break;
        }

    }

    /**
     * 处理 put 行为
     * @param ctx
     * @param msg
     */
    private void handlePut(ChannelHandlerContext ctx, SmallqMessage msg) {
        saveMessage(msg);
        AckMessage ackMessage = new AckMessage();
        ackMessage.setId(msg.getId());
        ackMessage.setTopic(msg.getTopic());
        ackMessage.setSuccess(true);
        ctx.channel().writeAndFlush(ackMessage);
    }

    /**
     * 处理 get 行为
     * @param ctx
     * @param msg
     */
    private void handleGet(ChannelHandlerContext ctx, SmallqMessage msg) {
        SmallqMessage smallqMessage = pollMessage(msg);
        if (smallqMessage == null) {

        }
        ctx.channel().writeAndFlush(smallqMessage);
    }

    /**
     * 初始化并返回队列
     * @param topic
     * @return
     */
    private synchronized Queue<SmallqMessage> initAndGetQueue(String topic) {
        Queue<SmallqMessage> queue = queueMap.get(topic);
        if (queue == null) {
            queue = new LinkedBlockingQueue<>();
            queueMap.put(topic, queue);
        }
        return queue;
    }

    /**
     * 保存消息到队列
     * @param msg
     */
    private boolean saveMessage(SmallqMessage msg) {
        Queue<SmallqMessage> queue = initAndGetQueue(getTopicFromMsg(msg));
        return queue.add(msg);
    }

    /**
     * 出队
     * @param msg
     * @return
     */
    private SmallqMessage pollMessage(SmallqMessage msg) {
        Queue<SmallqMessage> queue = initAndGetQueue(getTopicFromMsg(msg));
        return queue.poll();
    }

    /**
     * 获取话题
     * @param msg
     * @return
     */
    private String getTopicFromMsg(SmallqMessage msg) {
        String topic = msg.getTopic();
        return topic == null ? DEFAULT_TOPIC : topic;
    }
}
