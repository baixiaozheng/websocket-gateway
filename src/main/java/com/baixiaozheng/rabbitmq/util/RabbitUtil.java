package com.baixiaozheng.rabbitmq.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
@Slf4j
public class RabbitUtil {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 给queue发送消息
     *
     * @param queueName
     * @param msg
     */
    public void sendToQueue(String queueName, String msg) {

        sendToQueue(DirectExchange.DEFAULT, queueName, msg);
    }

    /**
     * 给direct交换机指定queue发送消息
     *
     * @param directExchange
     * @param queueName
     * @param msg
     */
    public void sendToQueue(DirectExchange directExchange, String queueName, String msg) {
        Queue queue = new Queue(queueName);
        addQueue(queue);
        Binding binding = BindingBuilder.bind(queue).to(directExchange).withQueueName();
        rabbitAdmin.declareBinding(binding);
        //设置消息内容的类型，默认是 application/octet-stream 会是 ASCII 码值
        rabbitTemplate.convertAndSend(directExchange.getName(), queueName, msg);
    }

    /**
     * 创建Exchange
     *
     * @param exchange
     */
    public void addExchange(AbstractExchange exchange) {
        rabbitAdmin.declareExchange(exchange);
    }

    /**
     * 创建一个指定的Queue
     *
     * @param queue
     * @return queueName
     */
    public String addQueue(Queue queue) {
        return rabbitAdmin.declareQueue(queue);
    }





}
