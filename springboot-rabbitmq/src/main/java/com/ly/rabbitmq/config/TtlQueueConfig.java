package com.ly.rabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @ClassName TtlQueueConfig
 * @Description TODO
 * @Author liaoyang
 * @Date 2022/3/22 14:50
 * @Version 1.0
 **/
@Configuration
public class TtlQueueConfig {

    /**
     * 普通队列交换机
     */
    private final static String X_EXCHANGE = "X";
    /**
     * 死信队列交换机
     */
    private final static String Y_DEAD_LETTER_EXCHANGE = "Y";
    /**
     * 普通队列 QA
     */
    private final static String QUEUE_A = "QA";
    /**
     * 普通队列 QB
     */
    private final static String QUEUE_B = "QB";
    /**
     * 死信队列 QD
     */
    private final static String DEAD_LETTER_QUEUE = "QD";


    /**
     * 声明 普通队列交换机
     * @return
     */
    @Bean("xExchange")
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }


    /**
     * 声明 死信队列交换机
     * @return
     */
    @Bean("yExchange")
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }


    /**
     * 声明 普通队列QA
     * @return
     */
    @Bean("queueA")
    public Queue queueA() {
        /**
         * 设置队列参数
         */
        HashMap<String, Object> arguments = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由key
        arguments.put("x-dead-letter-routing-key", "YD");
        //声明队列的 TTL
        arguments.put("x-message-ttl", 10000);

        return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }

    /**
     * 声明 普通队列QB
     * @return
     */
    @Bean("queueB")
    public Queue queueB() {
        /**
         * 设置队列参数
         */
        HashMap<String, Object> arguments = new HashMap<>(3);
        //声明当前队列绑定的死信交换机
        arguments.put("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE);
        //声明当前队列的死信路由key
        arguments.put("x-dead-letter-routing-key", "YD");
        //声明队列的 TTL
        arguments.put("x-message-ttl", 40000);

        return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }


    /**
     * 声明 死信队列
     * @return
     */
    @Bean("queueD")
    public Queue queueD() {
        return new Queue(DEAD_LETTER_QUEUE);
    }


    /**
     * 声明 队列A 绑定交换机
     * @param queue 队列
     * @param exchange 交换机
     * @return
     */
    @Bean
    public Binding queueaBindingX(@Qualifier("queueA") Queue queue,
                                  @Qualifier("xExchange") DirectExchange exchange) {
        //队列 - 绑定交换机 - 路由key
        return BindingBuilder.bind(queue).to(exchange).with("XA");
    }


    /**
     * 声明 队列B 绑定交换机
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding queuebBindingX(@Qualifier("queueB") Queue queue,
                                  @Qualifier("xExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("XB");
    }


    /**
     * 声明 死信队列 绑定交换机
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding  deadLetterBindingD(@Qualifier("queueD") Queue queue,
                                  @Qualifier("yExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("YD");
    }


}
