package com.ly.rabbitmq.ack;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.util.Scanner;

/**
 * @ClassName Task02
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/21 14:51
 * @Version 1.0
 **/
public class Task02 {

    public final static String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //不持久化
        //channel.queueDeclare(TASK_QUEUE_NAME, false, false, false, null);

        //队列持久化  保存到磁盘 保证了mq服务宕机重启 服务的消息依然存在
        boolean durable = true;
        channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();

            //消息不持久化
            //channel.basicPublish("", TASK_QUEUE_NAME, null, message.getBytes("UTF-8"));


            /**
             * 消息持久化 增加参数：MessageProperties.PERSISTENT_TEXT_PLAIN
             *
             * 消息发送成功 将消息保存到磁盘
             */
            channel.basicPublish("", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes("UTF-8"));

            System.out.println(message+":消息发送成功");
        }
    }

}
