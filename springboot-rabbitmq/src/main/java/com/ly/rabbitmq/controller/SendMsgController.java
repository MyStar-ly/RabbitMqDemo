package com.ly.rabbitmq.controller;

import com.ly.rabbitmq.config.DelayedQueueConfig;
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
     * 发送一个消息到延迟队列
     *
     * @param message
     */
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {

        log.info("当前时间：{} ---> 发送一个消息到两个ttl队列：{}", new Date().toString(), message);

        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列：" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列：" + message);

    }


    /**
     * 发送一个延迟消息
     *
     * @param message
     * @param time
     */
    @GetMapping("/sendDelayedMsg/{message}/{time}")
    public void sendDelayedMsg(@PathVariable String message, @PathVariable String time) {
        log.info("当前时间：{} ---> 发送一个时间为：{} 的消息到队列QC：{}", new Date().toString(), time, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
            //发送消息的时候 延迟时长
            msg.getMessageProperties().setExpiration(time);
            return msg;
        });
    }


    /**
     * 基于插件的延迟队列
     *
     * @param message
     * @param time
     */
    @GetMapping("/sendMsg/{message}/{time}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer time) {
        log.info("当前时间：{} ---> 发送一个时间为：{} 的延时消息到延时队列delayed.queue：{}", new Date().toString(), time.toString(), message);

        rabbitTemplate.convertAndSend(
                DelayedQueueConfig.DELAYED_EXCHANGE_NAME,
                DelayedQueueConfig.DELAYED_ROUTING_KEY,
                message,
                msg -> {
                    msg.getMessageProperties().setDelay(time);
                    return msg;
                });
    }

}
