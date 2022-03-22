package com.ly.rabbitmq.hello;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName Producer
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/21 11:25
 * @Version 1.0
 **/
public class Producer {

    public final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");

        //获取连接
        Connection connection = factory.newConnection();

        //创建一个channel
        Channel channel = connection.createChannel();

        //生成队列
        /**
         * 5大参数
         *
         * 1.队列名称
         * 2.队列是否持久化磁盘 默认存储内存
         * 3.队列只供一个消费者消费 是否消息共享 true：多个消费者 false:一个
         * 4.是否自动删除 最后一个消费着端开连接以后 该队列是否自动删除 true:是
         * 5.其他参数
         */
        AMQP.Queue.DeclareOk declareOk = channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //发送消息
        /**
         * 4大参数
         *
         * 1.发送的交换机
         * 2.路由key值
         * 3.其他参数
         * 4.消息体
         *
         */
        channel.basicPublish("", QUEUE_NAME, null, "hello world".getBytes());


        System.out.println("消息发动成功");
    }
}
