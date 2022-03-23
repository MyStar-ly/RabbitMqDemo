package com.ly.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DelayedQueueConfig
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/23 10:00
 * @Version 1.0
 **/
@Configuration
public class DelayedQueueConfig {

    /**
     * 延迟队列名称
     */
    public static final String DELAYED_QUEUE_NAME = "delayed.queue";
    /**
     * 延迟交换机
     */
    public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
    /**
     * 路由
     */
    public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";


    /**
     * 声明 一个延迟队列
     * @return
     */
    @Bean("delayedQueue")
    public Queue delayedQueue() {
        return new Queue(DELAYED_QUEUE_NAME);
    }


    /**
     * 声明 一个延迟交换机
     * @return
     */
    @Bean("delayedExchange")
    public CustomExchange delayedExchange() {

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-delayed-type", "direct");
        /**
         * 5大参数
         * 1.交换机名称
         * 2.交换机类型
         * 3.是否持久化
         * 4.是否自动删除
         * 5.其他的参数
         */
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, arguments);
    }


    /**
     * 绑定交换机
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindingDelayedQueue(@Qualifier("delayedQueue") Queue queue,
                                       @Qualifier("delayedExchange") CustomExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(DELAYED_ROUTING_KEY).noargs();
    }

}
