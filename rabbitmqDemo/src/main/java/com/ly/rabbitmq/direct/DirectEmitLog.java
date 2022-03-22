package com.ly.rabbitmq.direct;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import java.util.Scanner;

/**
 * @ClassName EmitLog
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 9:26
 * @Version 1.0
 **/
public class DirectEmitLog {

    private final static String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String message = scanner.next();

            channel.basicPublish(EXCHANGE_NAME, "warning" , null, message.getBytes("UTF-8"));

            System.out.println("生产者发出消息成功：" + message);
        }

    }
}
