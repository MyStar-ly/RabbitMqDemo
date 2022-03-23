package com.ly.rabbitmq.consumer;

import com.ly.rabbitmq.config.ConfirmConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @ClassName ConfirmConsumer
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/23 11:09
 * @Version 1.0
 **/
@Component
public class ConfirmConsumer {

    private final static Logger log = LoggerFactory.getLogger(ConfirmConsumer.class);


    /**
     * 接收消息
     * @param message
     * @throws Exception
     */
    @RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
    public void receiveMsg(Message message) throws Exception {

        String msg = new String(message.getBody(), "UTF-8");

        log.info("接受到队列 confirm.queue 消息:{}",msg);

    }

}
