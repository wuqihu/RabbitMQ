//package com.example.consumer;
//
//import com.example.consumer.config.RabbitConfiguration;
////import org.junit.jupiter.api.Test;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//@RunWith(SpringJUnit4ClassRunner.class)//让测试运行于Spring测试环境
//@ContextConfiguration(classes = RabbitConfiguration.class)//提供配置信息
//public class RabbitTest {
//
//    @Autowired
//    RabbitTemplate template;
//
//    @Test
//    public void send(){
//        String message = "您好，欢迎访问 pan_junbiao的博客";
//        //这里故意将routingKey参数写入错误，让其应发确认消息送到队列失败回调
//        template.convertAndSend("", "no_queue_name", message);
//    }
//}
