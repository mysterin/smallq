package com.mysterin.smallq.common.msg;

import lombok.Data;

@Data
public class SmallqMessage<T> extends BaseMessage {
    private T data;
}
