package cn.bravedawn.producer.component;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static org.springframework.amqp.rabbit.core.RabbitTemplate.*;

/**
 * @author : depers
 * @program : mall-rabbit
 * @description: 消息生产者
 * @date : Created in 2021/2/9 22:10
 */

@Component
public class RabbitSender {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 确认消息的回调监听接口，用于确认消息是否被broker所接收
     */
    final ConfirmCallback confirmCallback = new ConfirmCallback() {

        /**
         * 消息确认回调
         * @param correlationData 消息的唯一标识，用来确认是否为特定消息的确认回调
         * @param ack 是否落盘成功，是否到达broker
         * @param cause 失败的异常信息
         */
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            System.err.println("消息ACK结果：" + ack + ", correlationData: " + correlationData.getId());
        }
    };


    /**
     * 对外发送消息
     * @param message 具体的消息内容
     * @param properties 额外的附加属性
     * @throws Exception
     */
    public void send(Object message, Map<String, Object> properties) throws Exception {

        // MessageHeaders是对附加的属性信息进行封装
        MessageHeaders messageHeaders = new MessageHeaders(properties);
        Message<?> msg = MessageBuilder.createMessage(message, messageHeaders);

        // spring.rabbitmq.publisher-confirms=true, 设置消息确认回调的监听
        rabbitTemplate.setConfirmCallback(confirmCallback);

        // 指定业务唯一的ID
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        // 在执行消息转换后，可以在其中用于添加/修改标头或属性
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public org.springframework.amqp.core.Message postProcessMessage(org.springframework.amqp.core.Message message) throws AmqpException {
                System.out.println("---> post to do: " + message);
                return message;
            }
        };

        // 转换并发送消息
        rabbitTemplate.convertAndSend("exchange-1",
                            "springboot.rabbit",
                            msg,
                            messagePostProcessor,
                            correlationData
                            );
    }
}
