package com.foxiaotao.delayexecuter.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:
 * @author: xiaotao
 * @create: 2019-12-17 16:40
 */
@SpringBootTest
public class DelayEventServiceTest {
    @Autowired
    private DelayEventService delayEventService;

    @Test
    public void test() {
        for (int i = 0; i < 3; i++) {
            delayEventService.sendMessage(i + 1, "test message " + i);
        }
    }


}
