package com.ly.rabbitmq.direct;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * @ClassName ReceiveLogsDirect02
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 9:56
 * @Version 1.0
 **/
public class ReceiveLogsDirect02 {

    private final static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.queueDeclare("disk", false, false, false, null);
        channel.queueBind("disk", EXCHANGE_NAME, "error");

        System.out.println("ReceiveLogsDirect02等到接收消息：。。。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("ReceiveLogs02成功接收到了消息：" + new String(message.getBody(), "UTF-8"));
        };

        channel.basicConsume("disk", true, deliverCallback, (var) -> {});
    }


}
