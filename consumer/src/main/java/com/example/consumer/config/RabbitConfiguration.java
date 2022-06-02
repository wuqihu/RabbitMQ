package com.example.consumer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * RabbitMQ配置类
 **/
@Configuration
public class RabbitConfiguration {

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory  connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(true);
        System.out.println("CachingConnectionFactory");
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

//    @Bean
//    public Queue myQueue() {
//        return new Queue("myqueue");
//    }
//    @Bean
//    public Queue myQueue1() {
//        return new Queue("queue_name1");
//    }

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //确认消息送到交换机(Exchange)回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback()
        {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause)
            {
                System.out.println("\n确认消息送到交换机(Exchange)结果：");
                System.out.println("相关数据：" + correlationData);
                System.out.println("是否成功：" + ack);
                System.out.println("错误原因：" + cause);
            }
        });

        //确认消息送到队列(Queue)回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback()
        {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage)
            {
                System.out.println("\n确认消息送到队列(Queue)结果：");
                System.out.println("发生消息：" + returnedMessage.getMessage());
                System.out.println("回应码：" + returnedMessage.getReplyCode());
                System.out.println("回应信息：" + returnedMessage.getReplyText());
                System.out.println("交换机：" + returnedMessage.getExchange());
                System.out.println("路由键：" + returnedMessage.getRoutingKey());
            }
        });
        return rabbitTemplate;
    }

    @Bean
    public RabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(3);
        factory.setMaxConcurrentConsumers(10);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setPrefetchCount(1);
        return factory;
    }

    /**
     * 队列
     */
//    @Bean
//    public Queue queue()
//    {
//        /**
//         * 创建队列，参数说明：
//         * String name：队列名称。
//         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
//         * 持久化的队列会存盘，在服务器重启的时候不会丢失相关信息。
//         * boolean exclusive：设置是否排他，默认也是 false。为 true 则设置队列为排他。
//         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
//         * 当没有生产者或者消费者使用此队列，该队列会自动删除。
//         * Map<String, Object> arguments：设置队列的其他一些参数。
//         */
//        return new Queue("QUEUE_NAME", true, false, false, null);
//    }
//
//    /**
//     * Direct交换器
//     */
//    @Bean
//    public DirectExchange direct()
//    {
//        /**
//         * 创建交换器，参数说明：
//         * String name：交换器名称
//         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
//         * 持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
//         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
//         */
//        return new DirectExchange("exchange.direct", true, false);
//    }
//
//    @Bean
//    public DirectExchange direct1()
//    {
//        /**
//         * 创建交换器，参数说明：
//         * String name：交换器名称
//         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
//         * 持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
//         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
//         */
//        return new DirectExchange("exchange.direct1", true, false);
//    }
//
//    @Bean
//    public FanoutExchange fanout()
//    {
//        /**
//         * 创建交换器，参数说明：
//         * String name：交换器名称
//         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
//         * 持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
//         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
//         */
//        return new FanoutExchange("exchange.fanout", true, false);
//    }
//
//    @Bean
//    public TopicExchange topic()
//    {
//        /**
//         * 创建交换器，参数说明：
//         * String name：交换器名称
//         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
//         * 持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
//         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
//         */
//        return new TopicExchange("exchange.topic", true, false);
//    }

    /**
     * 绑定
     */
//    @Bean
//    Binding binding(DirectExchange exchange, Queue queue)
//    {
//        //将队列和交换机绑定, 并设置用于匹配键：routingKey
//        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
//    }
    //String destination【目的地】
    // Binding.DestinationType destinationType【目的地的类型】
    // String exchange【交换机名字】
    // String routingKey【路由键名字】
    // @Nullable Map<String, Object> arguments【自定义参数】
//    @Bean
//    Binding binding() {
//        //将队列和交换机绑定, 并设置用于匹配键：routingKey
//        return new Binding("queue_name1",
//                Binding.DestinationType.QUEUE,
//                "exchange.direct",
//                "hello.java",
//                null);
//    }
//
//    @Bean
//    Binding binding1() {
//        //将队列和交换机绑定, 并设置用于匹配键：routingKey
//        return new Binding(QUEUE_NAME,
//                Binding.DestinationType.QUEUE,
//                "exchange.direct1",
//                "hello.java",
//                null);
//    }
}