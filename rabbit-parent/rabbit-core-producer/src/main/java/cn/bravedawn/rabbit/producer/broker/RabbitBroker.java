package cn.bravedawn.rabbit.producer.broker;

import cn.bravedawn.rabbit.api.Message;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 具体发送不同种类型消息的接口
 * @date : Created in 2021/2/25 21:07
 */
public interface RabbitBroker {

    void rapidSend(Message message);

    void confirmSend(Message message);

    void reliantSend(Message message);

    void sendMessages();
}
