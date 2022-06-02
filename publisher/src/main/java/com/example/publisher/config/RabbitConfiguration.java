package com.example.publisher.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitMQ配置类
 **/
@Configuration
public class RabbitConfiguration {

    //连接rabbitmq配置
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

    /*
    * RabbitAdmin
    * */
    @Bean
    public RabbitAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }


    /*
     * RabbitTemplate
     * */
    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        //确认消息送到交换机(Exchange)回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback(){
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("\n确认消息送到交换机(Exchange)结果：");
                System.out.println("相关数据：" + correlationData);
                System.out.println("是否成功：" + ack);
                System.out.println("错误原因：" + cause);
            }
        });

        //确认消息送到队列(Queue)回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback(){
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
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

    /**
     * Direct交换器
     */
    @Bean
    public DirectExchange direct(){
        /**
         * 创建交换器，参数说明：
         * String name：交换器名称
         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
         * 持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
         */
        return new DirectExchange("exchange.direct", true, false);
    }

    /**
     * Fanout交换器
     */
    @Bean
    public FanoutExchange fanout(){
        return new FanoutExchange("exchange.fanout", true, false);
    }

    /**
     * Topic交换器
     */
    @Bean
    public TopicExchange topic(){
        return new TopicExchange("exchange.topic", true, false);
    }

    /**
     * 队列
     */
    @Bean
    public Queue ngbusiQueue(){
        /**
         * 创建队列，参数说明：
         * String name：队列名称。
         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
         * 持久化的队列会存盘，在服务器重启的时候不会丢失相关信息。
         * boolean exclusive：设置是否排他，默认也是 false。为 true 则设置队列为排他。
         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
         * 当没有生产者或者消费者使用此队列，该队列会自动删除。
         * Map<String, Object> arguments：设置队列的其他一些参数。
         */
        return new Queue("ngbusi", true, false, false, null);
    }

    @Bean
    public Queue ngbusiNewsQueue(){
        return new Queue("ngbusi.news", true, false, false, null);
    }
    @Bean
    public Queue ngbusiEmpsQueue(){
        return new Queue("ngbusi.emps", true, false, false, null);
    }
    @Bean
    public Queue ngxxNewsQueue(){
        return new Queue("ngxx.news", true, false, false, null);
    }

    /**
     * 绑定
     */
    @Bean
    public Binding directBinding(){
        //String destination【目的地】
        // Binding.DestinationType destinationType【目的地的类型】
        // String exchange【交换机名字】
        // String routingKey【路由键名字】
        // @Nullable Map<String, Object> arguments【自定义参数】
        return new Binding("ngbusi",
                Binding.DestinationType.QUEUE,
                "exchange.direct",
                "ngbusi",
                null);
    }


    @Bean
    public Binding directBinding1(){
        return new Binding("ngbusi",
                Binding.DestinationType.QUEUE,
                "exchange.direct",
                "ngbusi.news",
                null);
    }
    @Bean
    public Binding directBinding2(){
        return new Binding("ngbusi",
                Binding.DestinationType.QUEUE,
                "exchange.direct",
                "ngbusi.emps",
                null);
    }
    @Bean
    public Binding directBinding3(){
        return new Binding("ngbusi",
                Binding.DestinationType.QUEUE,
                "exchange.direct",
                "ngxx.news",
                null);
    }
    @Bean
    public Binding fanoutBinding(){
        return new Binding("ngbusi.news",
                Binding.DestinationType.QUEUE,
                "exchange.fanout",
                "ngbusi.news",
                null);
    }
    @Bean
    public Binding fanoutBinding1(){
        return new Binding("ngbusi.emps",
                Binding.DestinationType.QUEUE,
                "exchange.fanout",
                "ngbusi.emps",
                null);
    }
    @Bean
    public Binding fanoutBinding2(){
        return new Binding("ngbusi",
                Binding.DestinationType.QUEUE,
                "exchange.fanout",
                "ngbusi",
                null);
    }
    @Bean
    public Binding fanoutBinding3(){
        return new Binding("ngxx.news",
                Binding.DestinationType.QUEUE,
                "exchange.fanout",
                "ngxx.news",
                null);
    }
    @Bean
    public Binding topicBinding(){
        return new Binding("ngbusi",
                Binding.DestinationType.QUEUE,
                "exchange.topic",
                "ngbusi.#",
                null);
    }
    @Bean
    public Binding topicBinding1(){
        return new Binding("ngbusi.news",
                Binding.DestinationType.QUEUE,
                "exchange.topic",
                "ngbusi.#",
                null);
    }
    @Bean
    public Binding topicBinding2(){
        return new Binding("ngbusi.emps",
                Binding.DestinationType.QUEUE,
                "exchange.topic",
                "ngbusi.#",
                null);
    }
    @Bean
    public Binding topicBinding3(){
        return new Binding("ngxx.news",
                Binding.DestinationType.QUEUE,
                "exchange.topic",
                "*.news",
                null);
    }

    //延时队列
    @Bean
    public TopicExchange delayExchange(){
        return new TopicExchange("delay.exchange",true,false);
    }
    @Bean
    public Queue delayQueue(){
        Map<String,Object> arguments= new HashMap<>();
        arguments.put("x-dead-letter-exchange","delay.exchange");
        arguments.put("x-dead-letter-routing-key","delay.message");
        arguments.put("x-message-ttl",10000);

        return new Queue("delay.queue", true, false, false, arguments);
    }

    @Bean
    public Queue testQueue(){
        return new Queue("test.queue", true, false, false, null);
    }
    @Bean
    public Binding binding1(){
        return new Binding("delay.queue",
                Binding.DestinationType.QUEUE,
                "delay.exchange",
                "delay.queue",
                null);
    }
    @Bean
    public Binding binding2(){
        return new Binding("test.queue",
                Binding.DestinationType.QUEUE,
                "delay.exchange",
                "delay.message",
                null);
    }
}