package com.ly.rabbitmq.topic;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ClassName ReceiveLogsTopic01
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 10:32
 * @Version 1.0
 **/
public class ReceiveLogsTopic01 {

    private final static String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = "Q1";

        //声明队列
        channel.queueDeclare(queueName, false, false, false, null);

        //绑定交换机
        channel.queueBind(queueName, EXCHANGE_NAME, "*.orange.*");

        System.out.println("ReceiveLogsTopic01等待接收消息：。。。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogsTopic01成功接收到了消息----> 消息路由：" +message.getEnvelope().getRoutingKey() +"；消息内容："+ new String(message.getBody(), "UTF-8"));
        };

        channel.basicConsume(queueName, true, deliverCallback, (var1) -> {});
    }
}
