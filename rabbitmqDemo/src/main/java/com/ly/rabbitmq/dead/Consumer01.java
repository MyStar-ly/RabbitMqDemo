package com.ly.rabbitmq.dead;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Consumer01
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 11:30
 * @Version 1.0
 **/
public class Consumer01 {

    /**
     * 普通交换机
     */
    private final static String NORMAL_EXCHANGE = "normal_exchange";
    /**
     * 死信交换机
     */
    private final static String DEAD_EXCHANGE = "dead_exchange";
    /**
     * 普通队列
     */
    private final static String NORMAL_QUEUE = "normal_queue";
    /**
     * 死信队列
     */
    private final static String DEAD_QUEUE = "dead_queue";


    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        //设置其他参数
        Map<String, Object> arguments = new HashMap<>();
        //1.正常队列设置消息过期时间
        //2.正常队列设置死信交换机 参数 key 是固定值
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //3.正常队列设置死信 routing-key 参数 key 是固定值
        arguments.put("x-dead-letter-routing-key", "lisi");
        //设置队列最大长度
        //arguments.put("x-max-length", 6);

        //声明普通队列
        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);

        //声明死信队列
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        //绑定交换机
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");

        System.out.println("Consumer01等到接收消息：。。。");

        DeliverCallback deliverCallback = (consumerTag, message) -> {

            String msg = new String(message.getBody(), "UTF-8");
            if (msg.equals("info5")) {
                System.out.println("Consumer01成功接收到了消息：" + msg + ";此消息是被拒绝的");
                //拒绝消息
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);
            } else {
                System.out.println("Consumer01成功接收到了消息：" + msg);
                //应答
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }
        };

        //channel.basicConsume(NORMAL_QUEUE, true, deliverCallback, (var1)-> {});
        //消息拒绝必须采用手动应答
        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, (var1)-> {});

    }






}
