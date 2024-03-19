package cn.bravedawn.rabbit.producer.broker;

import cn.bravedawn.rabbit.api.Message;
import cn.bravedawn.rabbit.api.MessageType;
import cn.bravedawn.rabbit.producer.constant.BrokerMessageConst;
import cn.bravedawn.rabbit.producer.constant.BrokerMessageStatus;
import cn.bravedawn.rabbit.producer.entity.BrokerMessage;
import cn.bravedawn.rabbit.producer.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 不同类型消息发送的实现类
 * @date : Created in 2021/2/25 21:22
 */
@Component
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker{

    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;

    @Autowired
    private MessageStoreService messageStoreService;

    /**
     * 迅速发消息
     * @param message
     */
    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {
        message.setMessageType(MessageType.RELIANT);
        BrokerMessage bm = messageStoreService.selectByMessageId(message.getMessageId());
        if (bm == null) {
            // 1.将消息进行持久化
            Date now = new Date();
            BrokerMessage brokerMessage = new BrokerMessage();
            brokerMessage.setMessageId(message.getMessageId());
            brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
            // tryCount属性在最开始发送的时候不需要进行设置
            brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
            brokerMessage.setCreateTime(now);
            brokerMessage.setUpdateTime(now);
            brokerMessage.setMessage(message);
            messageStoreService.insert(brokerMessage);
        }
        // 2.执行真正的发送消息逻辑
        sendKernel(message);
    }

    @Override
    public void sendMessages() {
        List<Message> messages = MessageHolder.clear();
        messages.forEach(message -> {
            MessageHolderAsyncQueue.submit((Runnable) () -> {
                CorrelationData correlationData =
                        new CorrelationData(String.format("%s#%s#%s",
                                message.getMessageId(),
                                System.currentTimeMillis(),
                                message.getMessageType()));
                String topic = message.getTopic();
                String routingKey = message.getRoutingKey();
                RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
                rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
                log.info("#RabbitBrokerImpl.sendMessages# send to rabbitmq, messageId: {}", message.getMessageId());
            });
        });
    }

    /**
     * 发送消息的核心方法
     * @param message
     */
    private void sendKernel(Message message){
        // 异步提交
        AsyncBaseQueue.submit((Runnable) () -> {
            String topic = message.getTopic();
            String routingKey = message.getRoutingKey();
            CorrelationData correlationData = new CorrelationData(
                    String.format("%s#%s#%s",
                            message.getMessageId(),
                            System.currentTimeMillis(),
                            message.getMessageType()));
            RabbitTemplate template = rabbitTemplateContainer.getTemplate(message);
            template.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq, messageId: {}", message.getMessageId());
        });
    }
}
