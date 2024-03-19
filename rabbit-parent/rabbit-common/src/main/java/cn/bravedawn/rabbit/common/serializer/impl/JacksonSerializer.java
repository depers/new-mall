package cn.bravedawn.rabbit.common.serializer.impl;

import cn.bravedawn.rabbit.common.serializer.Serializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: Serializer接口的实现
 * @date : Created in 2021/2/27 14:04
 */
public class JacksonSerializer implements Serializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonSerializer.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();


    static {
        /**
         * 定义序列化方式
         */
        // 禁用 格式化输出
        MAPPER.disable(SerializationFeature.INDENT_OUTPUT);
        // 禁用 遇到未知属性，抛出JsonMappingException
        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        /**
         * 定义解析器功能
         */
        // 启用使用反斜杠引号机制接受所有字符的引号的功能
        MAPPER.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        // 允许在解析的内容内使用Java / C ++样式注释（'/'+'*'和'//'变体）的功能
        MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        // 允许解析器将“非数字”（NaN）令牌集识别为合法的浮点数值的功能
        MAPPER.configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);
        // 允许JSON整数以其他（可忽略的）零开头的功能（例如：000001）
        MAPPER.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
        // 允许单引号（撇号，字符“ \”）用于引用字符串（名称和字符串值）的功能
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 允许JSON字符串包含未加引号的控制字符（值小于32的ASCII字符，包括制表符和换行符）的功能
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        // 允许使用未加引号的字段名称的功能（Javascript允许，但JSON规范不允许）
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    private final JavaType type;

    private JacksonSerializer(JavaType type) {
        this.type = type;
    }

    public JacksonSerializer(Type type) {
        this.type = MAPPER.getTypeFactory().constructType(type);
    }

    public static JacksonSerializer createParametricType(Class cls) {
        return new JacksonSerializer(MAPPER.getTypeFactory().constructType(cls));
    }

    @Override
    public byte[] serializerRaw(Object data) {
        try {
            return MAPPER.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            LOGGER.error("序列化出错", e);
        }
        return null;
    }

    @Override
    public String serialize(Object data) {
        try {
            return MAPPER.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            LOGGER.error("序列化出错", e);
        }
        return null;
    }

    @Override
    public <T> T deserialize(String content) {
        try {
            return MAPPER.readValue(content, type);
        } catch (IOException e) {
            LOGGER.error("反序列化出错", e);
        }
        return null;
    }

    @Override
    public <T> T deserialize(byte[] content) {
        try {
            return MAPPER.readValue(content, type);
        } catch (IOException e) {
            LOGGER.error("反序列化出错", e);
        }
        return null;
    }
}
