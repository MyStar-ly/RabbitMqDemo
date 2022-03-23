package com.ly.rabbitmq.consumer;

import com.ly.rabbitmq.config.DelayedQueueConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @ClassName DelayQueueConsumer
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/23 10:21
 * @Version 1.0
 **/
@Component
public class DelayQueueConsumer {

    private final static Logger log = LoggerFactory.getLogger(DelayQueueConsumer.class);




    //接收插件的延迟队列消息
    @RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
    public void a(Message message) throws Exception {

        String msg = new String(message.getBody(), "UTF-8");

        log.info("当前时间：{},收到延时队列的消息：{}", new Date().toString(), msg);
    }
}
