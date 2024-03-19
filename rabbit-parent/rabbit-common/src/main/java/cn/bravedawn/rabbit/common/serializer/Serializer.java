package cn.bravedawn.rabbit.common.serializer;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 自定义序列化接口
 * @date : Created in 2021/2/27 14:00
 */
public interface Serializer{

    byte[] serializerRaw(Object data);

    String serialize(Object data);

    <T> T deserialize(String content);

    <T> T deserialize(byte[] content);

}
