package com.foxiaotao.delayexecuter.config.init;

import com.fasterxml.jackson.core.type.TypeReference;
import com.foxiaotao.delayexecuter.model.DelayTimeModel;
import com.foxiaotao.delayexecuter.util.JsonTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 初始化信息
 * 根据配置文件key=delay.time.el,初始化延迟阶梯时间
 * @author: xiaotao
 * @create: 2019-12-17 14:37
 */
@Slf4j
@Component
public class Init implements CommandLineRunner {

    @Value("${delay.time.el}")
    private String delayTimeStr;

    /**
     * key:重试的次数
     * value:重试的延迟时间（毫秒）
     */
    private static final Map<Integer, Long> retryDelayTimeMap = new ConcurrentHashMap(8);
    private static final Map<Integer, DelayTimeModel> delayUnit = new ConcurrentHashMap(8);


    @Override
    public void run(String... args) {
        Map<Integer, DelayTimeModel> map = JsonTools.deserialize(delayTimeStr, new TypeReference<Map<Integer, DelayTimeModel>>() {});
        log.info("延迟时间规则为：" + delayTimeStr);

        delayUnit.putAll(map);

        for (Map.Entry<Integer, DelayTimeModel> entry : map.entrySet()) {
            long millis = TimeoutUtils.toMillis(Long.valueOf(entry.getValue().getValue()), entry.getValue().getUnit());
            retryDelayTimeMap.put(entry.getKey(), millis);
        }

    }

    public static Map<Integer, Long> getRetryDelayTimeMap() {
        return retryDelayTimeMap;
    }

    public static Map<Integer, DelayTimeModel> getDelayUnit() {
        return delayUnit;
    }
}
