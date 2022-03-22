package com.ly.rabbitmq.dead;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Consumer01
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 11:30
 * @Version 1.0
 **/
public class Consumer02 {

    /**
     * 死信队列
     */
    private final static String DEAD_QUEUE = "dead_queue";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("Consumer01等到接收消息：。。。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer01成功接收到了消息：" + new String(message.getBody(), "UTF-8"));
        };

        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, (var1)-> {});

    }






}
