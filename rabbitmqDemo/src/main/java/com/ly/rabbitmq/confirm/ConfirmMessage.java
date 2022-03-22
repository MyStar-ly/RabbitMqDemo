package com.ly.rabbitmq.confirm;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.ConfirmListener;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @ClassName ConfirmMessage
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/21 16:00
 * @Version 1.0
 **/
public class ConfirmMessage {

    //消息确认
    private final static int MESSAGE_COUNT = 1000;
    private final static String CFMS_QUEUE = "cfms_queue";



    //测试
    public static void main(String[] args) throws Exception {

        //ConfirmMessage.individualConfirmation();

        ConfirmMessage.asynchronousMessage();

    }


    /**
     * 单个消息确认 / 批量
     */
    public static void individualConfirmation() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明队列
        channel.queueDeclare(CFMS_QUEUE, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        /**
         * 批量就在for循环当中加条件再确认
         */

        for (int i = 0; i < MESSAGE_COUNT; i++) {

            String message = i + "";

            channel.basicPublish("", CFMS_QUEUE, null, message.getBytes("UTF-8"));

            //单个消息确认
            boolean confirms = channel.waitForConfirms();

            if (confirms) {
                System.out.println(message + ":消息发送成功");
            }
        }


    }


    /**
     * 异步确认
     */
    public static void asynchronousMessage() throws Exception {
        Channel channel = RabbitMqUtils.getChannel();

        //声明队列
        channel.queueDeclare(CFMS_QUEUE, true, false, false, null);
        //开启发布确认
        channel.confirmSelect();

        //处理异步消息确认失败
        ConcurrentSkipListMap<Long, String> skipListMap = new ConcurrentSkipListMap<>();


        //消息确认成功 回调函数
        ConfirmCallback ackCallback = (deliveryTag, multiple) -> {

            //是否批量
            if (multiple) {
                //批量删除
                ConcurrentNavigableMap<Long, String> map = skipListMap.headMap(deliveryTag);
                map.clear();
            } else {
                //单个删除
                skipListMap.remove(deliveryTag);
            }

            System.out.println("确认的消息："+deliveryTag);
        };

        //消息确认失败 回调函数
        /**
         * 1.消息的标志
         * 2.是否为批量确认
         */
        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
            System.out.println("未确认的消息："+deliveryTag);
        };

        //异步确认监听器 监听那些消息成功 那些失败
        ConfirmListener listener = channel.addConfirmListener(ackCallback, nackCallback);

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";

            channel.basicPublish("", CFMS_QUEUE, null, message.getBytes("UTF-8"));

            //将消息添加到map
            skipListMap.put(channel.getNextPublishSeqNo(), message);
        }


    }

}
