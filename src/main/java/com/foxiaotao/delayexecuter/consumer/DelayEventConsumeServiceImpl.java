package com.foxiaotao.delayexecuter.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description: 收取消息实现
 * @author: xiaotao
 * @create: 2019-12-17 16:23
 */
@Slf4j
@Service
public class DelayEventConsumeServiceImpl implements DelayEventConsumeService {
    @Override
    public void consumeMessage(String message) {
        log.info("收到消息" + message);
    }
}
