package com.foxiaotao.delayexecuter;

import com.foxiaotao.delayexecuter.config.mq.RabbitMqPropertis;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DelayExecuterApplicationTests {


	@Resource(name = "delayRabbitTemplate")
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private RabbitMqPropertis rabbitMqPropertis;

	@Test
	void contextLoads() {
		rabbitTemplate.convertAndSend(rabbitMqPropertis.getDelayExchange(), rabbitMqPropertis.getDelayRoutingKey(),
				"test-meg", message -> {
					message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
					message.getMessageProperties().setExpiration("60000");
					return message;
				});
	}

}
