package cn.bravedawn.rabbit.common.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 装饰器模式-消息转换
 * @date : Created in 2021/2/27 15:10
 */
public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter delegate;

    // 装饰器模式：将原始对象作为一个参数传给装饰者的构造器
    public RabbitMessageConverter(GenericMessageConverter converter) {
        Preconditions.checkNotNull(converter);
        this.delegate = converter;
    }

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        // 将一个对象转换为Message时，这里可以做一些额外操作
        cn.bravedawn.rabbit.api.Message msg = (cn.bravedawn.rabbit.api.Message) object;
        // 设置延时时间
        messageProperties.setDelay(msg.getDelayMills());
        return this.delegate.toMessage(object, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        // 将Message转换为一个Java对象，这里可以做一些额外操作
        cn.bravedawn.rabbit.api.Message msg = (cn.bravedawn.rabbit.api.Message) this.delegate.fromMessage(message);
        return msg;
    }
}
