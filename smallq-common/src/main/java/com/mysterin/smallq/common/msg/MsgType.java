package com.mysterin.smallq.common.msg;

/**
 * @author linxiaobin
 * @Description 消息类型
 * @date 2022/11/16 10:19
 */
public enum MsgType {
    /**
     * 推送消息到队列
     */
    PUT,
    /**
     * 从队列获取消息
     */
    GET,
    /**
     * 应答消息
     */
    ACK
}
