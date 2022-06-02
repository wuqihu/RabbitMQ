//package com.example.consumer.service;
//
//import com.example.consumer.config.RabbitConfiguration;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
//public class MessageService {
//
//    @Autowired
//    RabbitTemplate template;
//
//
//    public static void main(String[] args) throws InterruptedException{
//            ApplicationContext context =
//                    new AnnotationConfigApplicationContext(RabbitConfiguration.class);
////        RabbitTemplate template = context.getBean(RabbitTemplate.class);
//        MessageService  messageService = new MessageService();
////        String message = "您好，欢迎访问 pan_junbiao的博客";
//        /*//这里故意将routingKey参数写入错误，让其应发确认消息送到队列失败回调
//        template.convertAndSend(RabbitConfiguration.EXCHANGE_NAME, "no_queue_name", message);*/
//
//        messageService.send();
//        //由于这里使用的是测试方法，当测试方法结束，RabbitMQ相关的资源也就关闭了，
//        //会导致消息确认的回调出现问题，所有加段延时
//        Thread.sleep(2000);
//    }
//
//    public void send(){
//        String message = "您好，欢迎访问 pan_junbiao的博客";
//        //这里故意将routingKey参数写入错误，让其应发确认消息送到队列失败回调
//        template.convertAndSend(RabbitConfiguration.EXCHANGE_NAME, "no_queue_name", message);
//    }
//}
package com.example.consumer.service;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageService {

    @RabbitListener(queues = {"test.queue"})
    public void recieveMessage(Message message, Channel channel){
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("接收到消息");
    }
}
