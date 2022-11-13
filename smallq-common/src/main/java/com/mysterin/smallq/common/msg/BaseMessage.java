package com.mysterin.smallq.common.msg;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/12 22:54
 */
@Data
@ToString
public class BaseMessage implements Serializable {
    private Long id;
    private String topic;
}
