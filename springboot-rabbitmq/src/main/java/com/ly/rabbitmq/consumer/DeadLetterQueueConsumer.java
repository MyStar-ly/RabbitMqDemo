package com.ly.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName DeadLetterQueueConsumer
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 15:37
 * @Version 1.0
 **/
@Component
public class DeadLetterQueueConsumer {

    private final static Logger log = LoggerFactory.getLogger(DeadLetterQueueConsumer.class);

    //接收消息
    @RabbitListener(queues = "QD")
    public void receiveD(Message message, Channel channel) throws Exception {

        String msg = new String(message.getBody(), "UTF-8");

        log.info("当前时间：{} ---> 接收到死信队列的消息：{}", new Date().toString(), msg);

    }

}
