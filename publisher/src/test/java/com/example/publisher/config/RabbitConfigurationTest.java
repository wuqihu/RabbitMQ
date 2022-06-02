package com.example.publisher.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)//让测试运行于Spring测试环境
@ContextConfiguration(classes = RabbitConfiguration.class)//提供配置信息
public class RabbitConfigurationTest {

    @Autowired
    RabbitTemplate template;

    //直接交换机
    @Test
    public void send() throws InterruptedException {
        String message = "交换机：exchange.direct 路由键：ngbusi";
        template.convertAndSend("exchange.direct", "ngbusi", message);
    }
    //扇出交换机测试
    @Test
    public void send1() throws InterruptedException {
        String message = "交换机：exchange.fanout 路由键：ngbusi";
        template.convertAndSend("exchange.fanout", "ngbusi", message);
    }

    //主题交换机测试
    @Test
    public void send2() throws InterruptedException {
        //#是匹配0个或者多个
        String message = "交换机：exchange.topic 路由键：ngbusi.news";
        template.convertAndSend("exchange.topic", "ngbusi.news", message);
    }
    @Test
    public void send3() throws InterruptedException {
        String message = "交换机：exchange.topic 路由键：hello.news";
        template.convertAndSend("exchange.topic", "hello.news", message);
    }


    //延时队列
    @Test
    public void send4() throws InterruptedException {
        String message = "hello";
        template.convertAndSend("delay.exchange", "delay.queue", message);
    }
}