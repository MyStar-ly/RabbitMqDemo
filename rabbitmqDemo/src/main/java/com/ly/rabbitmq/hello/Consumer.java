package com.ly.rabbitmq.hello;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Consumer
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/21 13:38
 * @Version 1.0
 **/
public class Consumer {

    public final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        //消息接收成功 回调函数
        DeliverCallback deliverCallback = (var1, var2) -> {
            System.out.println(new String(var2.getBody()));//打印消息体
        };

        //消息消费失败后 回调函数
        CancelCallback cancelCallback = var1 -> {
            System.out.println("消息消费失败");
        };

        /**
         * 4大参数
         *
         * 1.队列名称
         * 2.是否自动应答
         * 3.消息接收成功 回调函数
         * 4.消息消费失败后 回调函数
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }
}
