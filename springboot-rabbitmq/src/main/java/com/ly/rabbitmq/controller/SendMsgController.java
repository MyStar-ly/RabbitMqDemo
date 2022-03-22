package com.ly.rabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @ClassName SendMsgController
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 15:24
 * @Version 1.0
 **/
@RestController
@RequestMapping("/ttl")
public class SendMsgController {

    private final static Logger log = LoggerFactory.getLogger(SendMsgController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * 发送一个延迟消息
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {

        log.info("当前时间：{} ---> 发送一个消息到两个ttl队列：{}", new Date().toString(), message);

        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列：" + message);

    }

}
