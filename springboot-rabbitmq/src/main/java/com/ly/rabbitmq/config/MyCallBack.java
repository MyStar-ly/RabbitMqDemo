package com.ly.rabbitmq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

/**
 * @ClassName MyCallBack
 * @Description 交换机回调函数
 * @Author liaoyang
 * @Date 2022/3/23 11:23
 * @Version 1.0
 **/
@Component
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback {

    private final static Logger log = LoggerFactory.getLogger(MyCallBack.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @PostConstruct
    public void init() {
        //注入交换机回调接口
        rabbitTemplate.setConfirmCallback(this);
        //注入队列回退接口
        rabbitTemplate.setReturnCallback(this);
    }


    /**
     *  回调函数
     *  1.correlationData： 保存回调消息的id以及其他相关信息
     *  2.ack：  true成功   false失败
     *  3.cause： null       失败的原因
     *  接收成功消息
     *  接收失败消息
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        //获取消息id
        String id = correlationData != null ? correlationData.getId() : null;

        if (ack) {
            log.info("交换机已经收到 id 为:{}的消息",id);
        } else {
            log.info("交换机还未收到 id 为:{}消息,由于原因:{}",id,cause);
        }
    }


    /**
     *  回退函数
     *  失败才会调用
     * @param message 消息
     * @param replyCode
     * @param replyText 退回原因
     * @param exchange  交换机
     * @param routingKey    路由
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        try {
            log.info("消息:{}被服务器退回，退回原因:{}, 交换机是:{}, 路由 key:{}",
                    new String(message.getBody(), "UTF-8"),replyText, exchange, routingKey);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
