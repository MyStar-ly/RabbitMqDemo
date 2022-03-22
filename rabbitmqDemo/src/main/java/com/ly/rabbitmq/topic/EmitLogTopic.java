package com.ly.rabbitmq.topic;

import com.ly.rabbitmq.util.RabbitMqUtils;
import com.rabbitmq.client.Channel;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName EmitLogTopic
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 10:40
 * @Version 1.0
 **/
public class EmitLogTopic {
    private final static String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMqUtils.getChannel();

        /**
         * Q1-->绑定的是
         * 中间带 orange 带 3 个单词的字符串(*.orange.*)
         * Q2-->绑定的是
         * 最后一个单词是 rabbit 的 3 个单词(*.*.rabbit)
         * 第一个单词是 lazy 的多个单词(lazy.#)
         *
         */
        Map<String, String> map = new HashMap<>();
        map.put("quick.orange.rabbit","被队列 Q1Q2 接收到");
        map.put("lazy.orange.elephant","被队列 Q1Q2 接收到");
        map.put("quick.orange.fox","被队列 Q1 接收到");
        map.put("lazy.brown.fox","被队列 Q2 接收到");
        map.put("lazy.pink.rabbit","虽然满足两个绑定但只被队列 Q2 接收一次");
        map.put("quick.brown.fox","不匹配任何绑定不会被任何队列接收到会被丢弃");
        map.put("quick.orange.male.rabbit","是四个单词不匹配任何绑定会被丢弃");
        map.put("lazy.orange.male.rabbit","是四个单词但匹配 Q2");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String message = entry.getValue();

            channel.basicPublish(EXCHANGE_NAME, key, null, message.getBytes("UTF-8"));

            System.out.println("消息发送成功----->" + "消息路由："+key + "; 消息内容："+ message);
        }
    }
}
