package com.ly.rabbitmq.direct;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ClassName ReceiveLogsDirect01
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 9:50
 * @Version 1.0
 **/
public class ReceiveLogsDirect01 {

    private final static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.queueDeclare("console", false, false, false, null);
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");

        System.out.println("ReceiveLogsDirect01等到接收消息：。。。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs01成功接收到了消息：" + new String(message.getBody(), "UTF-8"));
        };

        channel.basicConsume("console", true, deliverCallback, (var) -> {});
    }

}
