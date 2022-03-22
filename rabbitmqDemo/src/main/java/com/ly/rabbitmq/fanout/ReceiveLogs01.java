package com.ly.rabbitmq.fanout;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ClassName ReceiveLogs01
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 9:13
 * @Version 1.0
 **/
public class ReceiveLogs01 {

    private final static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //订阅/发布模式 交换机
        /**
         * 1.交换机名称
         * 2.交换机类型
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        /**
         * 声明一个临时队列
         * 断开就删除该队列
         */
        String queueName = channel.queueDeclare().getQueue();


        /**
         * 队列绑定交换机
         * 1. 队列名称
         * 2. 交换机名称
         * 3. routingKey
         */
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println("ReceiveLogs01等待接收消息,把接收到的消息打印在屏幕.....");

        DeliverCallback deliverCallback = (consumerTag, message) -> {

            System.out.println("ReceiveLogs01成功接收到了消息：" + new String(message.getBody(), "UTF-8"));

            //channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(queueName, true, deliverCallback, (consumerTag) -> {});
    }
}
