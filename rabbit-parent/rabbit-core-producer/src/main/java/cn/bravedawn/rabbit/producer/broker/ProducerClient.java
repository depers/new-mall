package cn.bravedawn.rabbit.producer.broker;

import cn.bravedawn.rabbit.api.Message;
import cn.bravedawn.rabbit.api.MessageProducer;
import cn.bravedawn.rabbit.api.MessageType;
import cn.bravedawn.rabbit.api.SendCallback;
import cn.bravedawn.rabbit.exception.MessageRunTimeException;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 消息发送客户端
 * @date : Created in 2021/2/25 21:02
 */
@Component
public class ProducerClient implements MessageProducer {

    @Autowired
    private RabbitBroker rabbitBroker;

    @Override
    public void send(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        String messageType = message.getMessageType();
        switch (messageType){
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {
        messages.forEach(message -> {
            message.setMessageType(MessageType.RAPID);
            MessageHolder.add(message);
        });

        rabbitBroker.sendMessages();
    }


    @Override
    public void send(Message message, SendCallback sendCallback) throws MessageRunTimeException {

    }
}
