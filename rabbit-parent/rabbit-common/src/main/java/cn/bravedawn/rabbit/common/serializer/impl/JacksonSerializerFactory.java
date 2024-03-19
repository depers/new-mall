package cn.bravedawn.rabbit.common.serializer.impl;

import cn.bravedawn.rabbit.api.Message;
import cn.bravedawn.rabbit.common.serializer.Serializer;
import cn.bravedawn.rabbit.common.serializer.SerializerFactory;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 序列化工厂实现
 * @date : Created in 2021/2/27 14:26
 */
public class JacksonSerializerFactory implements SerializerFactory {

    // 饿汉式单例
    public static final SerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }

}
