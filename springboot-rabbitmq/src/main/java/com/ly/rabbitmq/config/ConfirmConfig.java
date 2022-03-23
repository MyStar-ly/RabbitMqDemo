package com.ly.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ConfirmConfig
 * @Description 确认消息发送成功
 * @Author liaoyang
 * @Date 2022/3/23 10:51
 * @Version 1.0
 **/
@Configuration
public class ConfirmConfig {

    /**
     * 交换机
     */
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    /**
     * 队列
     */
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    /**
     * 路由
     */
    public static final String CONFIRM_ROUTING_KEY = "key1";

    /**
     * 备份交换机
     */
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    /**
     * 备份队列
     */
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    /**
     * 警告队列
     */
    public static final String WARNING_QUEUE_NAME = "warning.queue";


    /**
     * 声明队列
     *
     * @return
     */
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }


    /**
     * 声明交换机
     *
     * @return
     */
    /*@Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).build();
    }*/
    //设置备份交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                //是否持久化
                .durable(true)
                //设置该交换机的备份交换机
                .withArgument("alternate-exchange", BACKUP_EXCHANGE_NAME)
                .build();
    }


    /**
     * 绑定队列交换机路由
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding queueBinding(@Qualifier("confirmQueue") Queue queue,
                                @Qualifier("confirmExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(CONFIRM_ROUTING_KEY);
    }


    /*=========================================配置备份交换机=================================================*/

    /**
     * 声明备份队列
     *
     * @return
     */
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    /**
     * 声明警告队列
     *
     * @return
     */
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    /**
     * 声明备份交换机
     *
     * @return
     */
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return ExchangeBuilder.fanoutExchange(BACKUP_EXCHANGE_NAME).build();
    }

    /**
     * 绑定备份队列 备份交换机
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding backupQueueBinding(@Qualifier("backupQueue") Queue queue,
                                      @Qualifier("backupExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    /**
     * 绑定警告队列 备份交换机
     *
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding warningQueueBinding(@Qualifier("warningQueue") Queue queue,
                                       @Qualifier("backupExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

}
