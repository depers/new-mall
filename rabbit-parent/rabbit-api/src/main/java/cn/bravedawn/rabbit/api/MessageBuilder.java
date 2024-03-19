package cn.bravedawn.rabbit.api;

import cn.bravedawn.rabbit.exception.MessageException;
import cn.bravedawn.rabbit.exception.MessageRunTimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 消息构建者
 * @date : Created in 2021/2/24 22:15
 */
public class MessageBuilder {

    private String messageId;
    private String topic;
    private String routingKey = "";
    private Map<String, Object> attributes = new HashMap<>();
    private int delayMills;
    private String messageType = MessageType.CONFIRM;

    private MessageBuilder(){
    }

    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    public MessageBuilder withMessageId(String messageId){
        this.messageId = messageId;
        return this;
    }

    public MessageBuilder withTopic(String topic){
        this.topic = topic;
        return this;
    }

    public MessageBuilder withRoutingKey(String routingKey){
        this.routingKey = routingKey;
        return this;
    }

    public MessageBuilder withAttributes(Map<String, Object> attributes){
        this.attributes = attributes;
        return this;
    }

    public MessageBuilder withDelayMills(int delayMills){
        this.delayMills = delayMills;
        return this;
    }

    public MessageBuilder withMessageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public Message build(){
        // 1.check messageId
        if (messageId == null) {
            messageId = UUID.randomUUID().toString();
        }

        // 2. if topic is null
        if (topic == null) {
            throw new MessageRunTimeException("this topic is null");
        }

        Message message = new Message(messageId, topic, routingKey, attributes,delayMills, messageType);
        return message;
    }

}
