package com.foxiaotao.delayexecuter.config.mq;

import com.foxiaotao.delayexecuter.consumer.DelayEventConsumeService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: mq对应bean初始化，队列，exchange
 * 需要注入配置rabbitMqPropertis，rabbitMqPropertis是mq的基础配置信息
 * @author: xiaotao
 * @create: 2019-12-17 14:37
 */
@Component
public class RabbitMqConfig {

    @Autowired
    private RabbitMqPropertis rabbitMqPropertis;

    @Bean("delayConnectionFactory")
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMqPropertis.getHost(), rabbitMqPropertis.getPort());
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        connectionFactory.setChannelCacheSize(180 * 1000);
        connectionFactory.setUsername(rabbitMqPropertis.getUsername());
        connectionFactory.setPassword(rabbitMqPropertis.getPassword());
        connectionFactory.setVirtualHost(rabbitMqPropertis.getVirtualHost());
        connectionFactory.setPublisherReturns(false);
        return connectionFactory;
    }


    /**
     * 申明死信队列
     * @return
     */
    @Bean("dlxExchangeBean")
    public DirectExchange dlxExchangeBean() {
        return new DirectExchange(rabbitMqPropertis.getDlxExchange());
    }

    @Bean("dlxQueueBean")
    public Queue dlxQueueBean() {
        return new Queue(rabbitMqPropertis.getDlxQueue());
    }


    @Bean
    public Binding binding(@Qualifier("dlxExchangeBean") DirectExchange dlxExchange, @Qualifier("dlxQueueBean") Queue dlxQueue) {
        return BindingBuilder.bind(dlxQueue).to(dlxExchange).with(rabbitMqPropertis.getDlxRoutingKey());
    }




    /**
     * 申明业务队列
     * @return
     */
    @Bean("delayExchange")
    public DirectExchange delayExchangeBean() {
        return new DirectExchange(rabbitMqPropertis.getDelayExchange());
    }

    @Bean("delayQueue")
    public Queue orderQueueBean() {
        Map<String,Object> arguments = new HashMap<>(4);
        // 绑定该队列到死信交换机
        arguments.put("x-dead-letter-exchange", rabbitMqPropertis.getDlxExchange());
        arguments.put("x-dead-letter-routing-key", rabbitMqPropertis.getDlxRoutingKey());
        return new Queue(rabbitMqPropertis.getDelayQueue(),true,false,false, arguments);
    }

    @Bean
    public Binding orderBinding(@Qualifier("delayExchange") DirectExchange delayExchange, @Qualifier("delayQueue") Queue delayQueue) {
        return BindingBuilder.bind(delayQueue).to(delayExchange).with(rabbitMqPropertis.getDelayRoutingKey());
    }


    @Bean(name = "delayRabbitTemplate")
    public RabbitTemplate delayRabbitTemplate(@Qualifier("delayConnectionFactory") ConnectionFactory connectionFactory, @Qualifier("delayExchange") DirectExchange delayExchange) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 默认 Exchange
        template.setExchange(delayExchange.getName());
        return template;
    }


    /**
     * 订单状态消息处理监听
     * @param delayEventConsumeService
     * @param cachingConnectionFactory
     * @param dlxQueue
     * @return
     */
    @Bean("delayDlxSimpleMessageListenerContainer")
    public SimpleMessageListenerContainer syncChannelApplySimpleMessageListenerContainer(@Autowired DelayEventConsumeService delayEventConsumeService,
                                                                                         @Qualifier("delayConnectionFactory") CachingConnectionFactory cachingConnectionFactory,
                                                                                         @Qualifier("dlxQueueBean")Queue dlxQueue) {
        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
        simpleRabbitListenerContainerFactory.setConnectionFactory(cachingConnectionFactory);
        SimpleRabbitListenerEndpoint simpleRabbitListenerEndpoint = new SimpleRabbitListenerEndpoint();
        simpleRabbitListenerEndpoint.setQueues(dlxQueue);
        simpleRabbitListenerEndpoint.setMessageListener(new MessageListenerAdapter(delayEventConsumeService, "consumeMessage"));
        SimpleMessageListenerContainer container = simpleRabbitListenerContainerFactory.createListenerContainer(simpleRabbitListenerEndpoint);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.start();
        return container;
    }


}
