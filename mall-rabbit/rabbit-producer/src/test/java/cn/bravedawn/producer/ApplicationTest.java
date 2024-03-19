package cn.bravedawn.producer;

import cn.bravedawn.producer.component.RabbitSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : depers
 * @program : mall-rabbit
 * @description: 测试类
 * @date : Created in 2021/2/19 21:39
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTest {

    @Autowired
    private RabbitSender rabbitSender;

    @Test
    public void testSender() throws Exception {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("attr1", "12345");
        properties.put("attr2", "abcde");
        rabbitSender.send("hello rabbitmq!", properties);

        Thread.sleep(10000);
    }

}
