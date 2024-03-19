package cn.bravedawn.rabbit.api;

import cn.bravedawn.rabbit.exception.MessageRunTimeException;

import java.util.List;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 消息生产者
 * @date : Created in 2021/2/25 19:59
 */
public interface MessageProducer {

    /**
     * message消息的发送
     * @param message
     * @throws MessageRunTimeException
     */
    void send(Message message) throws MessageRunTimeException;

    /**
     * 批量消息的发送
     * @param messages
     * @throws MessageRunTimeException
     */
    void send(List<Message> messages) throws MessageRunTimeException;

    /**
     * 消息的发送，附带SendCallback回调执行响应的业务逻辑处理
     * @param message
     * @param sendCallback
     * @throws MessageRunTimeException
     */
    void send(Message message, SendCallback sendCallback) throws MessageRunTimeException;

}
