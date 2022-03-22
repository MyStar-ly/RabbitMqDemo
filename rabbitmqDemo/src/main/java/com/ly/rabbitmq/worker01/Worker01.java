package com.ly.rabbitmq.worker01;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ClassName Worker02
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/21 14:05
 * @Version 1.0
 **/
public class Worker01 {

    public final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        DeliverCallback deliverCallback = (var1, message) -> {
            System.out.println("消费的消息："+new String(message.getBody()));
        };

        CancelCallback cancelCallback = var1 -> {
            System.out.println("消息消费失败回调：" + var1);
        };

        System.out.println("C2---等待消费消息。。。");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);
    }
}
