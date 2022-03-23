package com.ly.rabbitmq.consumer;

import com.ly.rabbitmq.config.ConfirmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @ClassName WarningConsumer
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/23 13:26
 * @Version 1.0
 **/
@Component
public class WarningConsumer {

    private final static Logger log = LoggerFactory.getLogger(WarningConsumer.class);


    /**
     * 警告队列
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = ConfirmConfig.WARNING_QUEUE_NAME)
    public void receiveWarningMsg(Message message) throws Exception {

        String msg = new String(message.getBody(), "UTF-8");

        log.error("报警发现不可路由消息：{}", msg);
    }

}
