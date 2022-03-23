package com.ly.rabbitmq.controller;

import com.ly.rabbitmq.config.ConfirmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ConfirmController
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/23 11:04
 * @Version 1.0
 **/
@RestController
@RequestMapping("confirm")
public class ConfirmController {

    private final static Logger log = LoggerFactory.getLogger(ConfirmController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 发送消息
     * @param message
     */
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message) {

        //测试消息发布交换机确认
        CorrelationData correlationData = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY, message, correlationData);

        log.info("发送消息内容:{}, 路由：key1",message);


        //测试消息发布交换机确认
        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME, ConfirmConfig.CONFIRM_ROUTING_KEY+"123", message, correlationData2);

        log.info("发送消息内容:{}, 路由：key1123",message);
    }

}
