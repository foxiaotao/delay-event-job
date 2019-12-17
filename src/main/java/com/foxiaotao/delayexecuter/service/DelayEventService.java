package com.foxiaotao.delayexecuter.service;


/**
 * @description: 延迟事件发送接口
 * @author: xiaotao
 * @create: 2019-12-17 14:26
 */
public interface DelayEventService {

    /**
     * 发送延迟事件
     * @param retryTimes: 当前第几次重试
     * @param messageContent：重试需要的消息内容
     * @return
     */
    void sendMessage(Integer retryTimes, String messageContent);

}
