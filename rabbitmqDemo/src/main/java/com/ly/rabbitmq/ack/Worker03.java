package com.ly.rabbitmq.ack;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName Worker03
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/21 14:46
 * @Version 1.0
 **/
public class Worker03 {

    public final static String TASK_QUEUE_NAME = "ack_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        System.out.println("C2---长时间等待接收消息。。。");

        //接收消息
        DeliverCallback deliverCallback = (var1, message) -> {

            //睡一秒
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("接收到的消息：" + new String(message.getBody(), "UTF-8"));

            /**
             * 2个参数
             *
             * 1.消息的标记
             * 2.是否批量应答
             *
             */
            channel.basicAck(message.getEnvelope().getDeliveryTag() , false);
        };


        //消费失败回调
        CancelCallback cancelCallback = var1 -> {
            System.out.println(var1 + ": 消费着消费失败回调");
        };

        //设置不公平分发
        //channel.basicQos(1);
        //设置预取值
        channel.basicQos(5);

        channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, cancelCallback);
    }

}
