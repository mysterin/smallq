package com.mysterin.smallq.common.msg;

/**
 * @author linxiaobin
 * @Description 消息行为
 * @date 2022/11/16 10:19
 */
public enum Action {
    /**
     * 推送消息到队列
     */
    PUT,
    /**
     * 从队列获取消息
     */
    GET
}
