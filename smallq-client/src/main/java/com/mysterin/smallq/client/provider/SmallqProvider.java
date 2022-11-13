package com.mysterin.smallq.client.provider;

import com.mysterin.smallq.client.SmallqClient;
import com.mysterin.smallq.common.msg.AckMessage;
import com.mysterin.smallq.common.msg.BaseMessage;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/13 19:15
 */
@Slf4j
public class SmallqProvider {

    private SmallqClient smallqClient;

    public SmallqProvider(SmallqClient smallqClient) {
        this.smallqClient = smallqClient;
    }

    public AckMessage send(BaseMessage baseMessage) {
        AckMessage ackMessage = smallqClient.send(baseMessage);
        return ackMessage;
    }

    public static void main(String[] args) {
        SmallqProvider smallqProvider = new SmallqProvider(new SmallqClient("127.0.0.1", 22333));
        long start = System.currentTimeMillis();
        for (int i=0; i<100000; i++) {
            BaseMessage message = new BaseMessage();
            message.setId((long) i);
            message.setTopic("SMALLQ-TOPIC");
            smallqProvider.send(message);
        }
        long end = System.currentTimeMillis();
        log.info("耗时：{}", end - start);
    }
}
