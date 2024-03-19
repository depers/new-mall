package cn.bravedawn.rabbit;

import cn.bravedawn.rabbit.api.Message;
import cn.bravedawn.rabbit.api.MessageType;
import cn.bravedawn.rabbit.producer.broker.ProducerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 测试发送消息
 * @date : Created in 2021/3/16 21:00
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private ProducerClient producerClient;

    @Test
    public void testProductClientSuccess() throws InterruptedException {
        String uniqueId = UUID.randomUUID().toString();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "冯晓");
        attributes.put("age", 18);

        Message message = new Message(uniqueId, "exchange-1", "SpringBoot.abc", attributes, 0);
        message.setMessageType(MessageType.RELIANT);
        producerClient.send(message);

        Thread.sleep(10000);

    }

    @Test
    public void testProductClientFailure() throws InterruptedException {
        String uniqueId = UUID.randomUUID().toString();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "冯晓");
        attributes.put("age", 18);

        Message message = new Message(uniqueId, "exchange-2", "SpringBoot.abc", attributes, 0);
        message.setMessageType(MessageType.RELIANT);
        producerClient.send(message);

        Thread.sleep(100000);
    }

    @Test
    public void testDelayMsg() throws InterruptedException {
        String uniqueId = UUID.randomUUID().toString();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "冯晓");
        attributes.put("age", 18);

        // 设置延迟时间为15s
        Message message = new Message(uniqueId,
                "delay-exchange", "delay.abc", attributes, 15000);
        message.setMessageType(MessageType.RELIANT);
        producerClient.send(message);

        Thread.sleep(100000);

    }
}
