package cn.bravedawn.rabbit.producer.broker;

import cn.bravedawn.rabbit.api.Message;
import cn.bravedawn.rabbit.api.MessageType;
import cn.bravedawn.rabbit.common.convert.GenericMessageConverter;
import cn.bravedawn.rabbit.common.convert.RabbitMessageConverter;
import cn.bravedawn.rabbit.common.serializer.Serializer;
import cn.bravedawn.rabbit.common.serializer.SerializerFactory;
import cn.bravedawn.rabbit.common.serializer.impl.JacksonSerializerFactory;
import cn.bravedawn.rabbit.producer.service.MessageStoreService;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: RabbitTemplateContainer池化封装
 * @date : Created in 2021/2/26 21:15
 *
 *  每一个topic 对应一个RabbitTemplate
 *  1. 提高发送的效率
 *  2. 可以根据不同的需求制定化不同的RabbitTemplate, 比如每一个topic 都有自己的routingKey规则
 */
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

    private Map<String /* topic */, RabbitTemplate> container = Maps.newHashMap();

    private Splitter splitter = Splitter.on("#");

    // 这里用到了工厂模式
    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private MessageStoreService messageStoreService;

    public RabbitTemplate getTemplate(Message message) {
        Preconditions.checkNotNull(message);
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = container.get(topic);

        if (rabbitTemplate != null) {
            return rabbitTemplate;
        }
        log.info("#RabbitTemplateContainer.getTemplate# topic: {} is not exists, create one.", topic);

        // 定制化RabbitTemplate
        RabbitTemplate newRabbitTemplate = new RabbitTemplate(connectionFactory);
        newRabbitTemplate.setExchange(topic);
        newRabbitTemplate.setRoutingKey(message.getRoutingKey());
        newRabbitTemplate.setRetryTemplate(new RetryTemplate());

        // 添加序列化反序列化和converter对象
        Serializer serializer = serializerFactory.create();
        GenericMessageConverter gmc = new GenericMessageConverter(serializer);
        RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
        newRabbitTemplate.setMessageConverter(rmc);

        String messageType = message.getMessageType();
        // 除快速消息之外，设置消息确认回调
        if (!MessageType.RAPID.equals(message.getMessageType())){
            newRabbitTemplate.setConfirmCallback(this);
        }

        container.putIfAbsent(topic, newRabbitTemplate);
        return newRabbitTemplate;
    }


    /**
     * 回调确认
     * 无论是 confirm 消息 还是 reliant 消息，发送消息以后 broker都会去回调confirm
     * @param correlationData 关联数据
     * @param ack 是否成功
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        // 具体的消息应答
        List<String> str = splitter.splitToList(correlationData.getId());
        String messageId = str.get(0);
        long sendTime = Long.parseLong(str.get(1));
        String messageType = str.get(2);
        if (ack) {
            //	当Broker 返回ACK成功时, 就是更新一下日志表里对应的消息发送状态为 SEND_OK

            // 	如果当前消息类型为reliant 我们就去数据库查找并进行更新
            if(MessageType.RELIANT.endsWith(messageType)) {
                messageStoreService.success(messageId);
            }
            log.info("send message is OK, confirm messageId: {}, sendTime: {}, messageType={}.", messageId, sendTime, messageType);
        } else {
            log.error("send message is Fail, confirm messageId: {}, sendTime: {}, messageType={}.", messageId, sendTime, messageType);
        }
    }
}
