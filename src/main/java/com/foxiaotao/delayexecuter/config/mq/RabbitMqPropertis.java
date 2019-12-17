package com.foxiaotao.delayexecuter.config.mq;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties(prefix = "tt.rabbitmq")
public class RabbitMqPropertis {

    private String host;
    private String virtualHost;
    private String username;
    private String password;
    private int port;

    private String delayExchange;
    private String delayQueue;
    private String delayRoutingKey;
    private String dlxExchange;
    private String dlxQueue;
    private String dlxRoutingKey;

    private String cacheMode;

    public String getCacheMode() {
        return cacheMode;
    }

    public void setCacheMode(String cacheMode) {
        this.cacheMode = cacheMode;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getVirtualHost() {
        return virtualHost;
    }

    public void setVirtualHost(String virtualHost) {
        this.virtualHost = virtualHost;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDelayExchange() {
        return delayExchange;
    }

    public void setDelayExchange(String delayExchange) {
        this.delayExchange = delayExchange;
    }

    public String getDelayQueue() {
        return delayQueue;
    }

    public void setDelayQueue(String delayQueue) {
        this.delayQueue = delayQueue;
    }

    public String getDelayRoutingKey() {
        return delayRoutingKey;
    }

    public void setDelayRoutingKey(String delayRoutingKey) {
        this.delayRoutingKey = delayRoutingKey;
    }

    public String getDlxExchange() {
        return dlxExchange;
    }

    public void setDlxExchange(String dlxExchange) {
        this.dlxExchange = dlxExchange;
    }

    public String getDlxQueue() {
        return dlxQueue;
    }

    public void setDlxQueue(String dlxQueue) {
        this.dlxQueue = dlxQueue;
    }

    public String getDlxRoutingKey() {
        return dlxRoutingKey;
    }

    public void setDlxRoutingKey(String dlxRoutingKey) {
        this.dlxRoutingKey = dlxRoutingKey;
    }
}
