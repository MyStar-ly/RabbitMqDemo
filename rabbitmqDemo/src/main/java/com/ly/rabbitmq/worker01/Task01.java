package com.ly.rabbitmq.worker01;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @ClassName Task02
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/21 14:19
 * @Version 1.0
 **/
public class Task01 {
    public final static String QUEUE_NAME = "hello";
    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            System.out.println("消息发送成功：" + message);
        }
    }
}
