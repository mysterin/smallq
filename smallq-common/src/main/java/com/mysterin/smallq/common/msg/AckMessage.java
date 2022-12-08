package com.mysterin.smallq.common.msg;

import lombok.Data;
import lombok.ToString;

/**
 * @author linxiaobin
 * @Description
 * @date 2022/11/13 21:37
 */
@Data
@ToString
public class AckMessage extends SmallqMessage {
    private boolean success;
}
