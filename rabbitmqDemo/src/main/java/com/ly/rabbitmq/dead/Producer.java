package com.ly.rabbitmq.dead;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

/**
 * @ClassName Producer
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 11:41
 * @Version 1.0
 **/
public class Producer {

    /**
     * 普通队列
     */
    private final static String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        //设置消息过期时间
        //AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

        for (int i = 1; i < 11; i++) {
            String message = "info"+i;

            //channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", properties, message.getBytes("UTF-8"));
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes("UTF-8"));

            System.out.println("消息发送成功：" + message);
        }

    }

}
