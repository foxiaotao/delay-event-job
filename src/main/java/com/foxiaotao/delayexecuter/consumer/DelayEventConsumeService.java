package com.foxiaotao.delayexecuter.consumer;


/**
 * @description: 延迟事件消息接受接口
 * @author: xiaotao
 * @create: 2019-12-17 14:26
 */
public interface DelayEventConsumeService {

    void consumeMessage(String message);
}
