package com.mysterin.smallq.client.consumer;

import com.mysterin.smallq.client.SmallqClient;
import com.mysterin.smallq.common.msg.SmallqMessage;
import lombok.Data;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/22 14:27
 */
@Data
public abstract class AbstractSmallqConsumer {

    private SmallqClient smallqClient;

    private String topic;


    public void init() {

    }

    public void consume(SmallqMessage smallqMessage) {

    }
}

