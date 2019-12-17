package com.foxiaotao.delayexecuter.service;

import com.foxiaotao.delayexecuter.config.init.Init;
import com.foxiaotao.delayexecuter.config.mq.RabbitMqPropertis;
import com.foxiaotao.delayexecuter.util.JsonTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: rabbit mq 作为延迟处理机制
 * @author: xiaotao
 * @create: 2019-12-17 14:30
 */
@Slf4j
@Service("rabbitMqDelayServiceImpl")
public class RabbitMqDelayServiceImpl implements DelayEventService {


    @Resource(name = "delayRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMqPropertis rabbitMqPropertis;

    @Override
    public void sendMessage(Integer retryTimes, String messageContent) {

        Long delayMillis = Init.getRetryDelayTimeMap().get(retryTimes);

        rabbitTemplate.convertAndSend(rabbitMqPropertis.getDelayExchange(), rabbitMqPropertis.getDelayRoutingKey(),
                messageContent, message -> {
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    message.getMessageProperties().setExpiration(delayMillis + "");
                    return message;
                });
        log.info("[delayMessageSend][RabbitMqDelay]延迟消息已发送, 延迟时间为:{},消息体为:{}", JsonTools.serialize(Init.getDelayUnit().get(retryTimes)), messageContent);
    }

}
