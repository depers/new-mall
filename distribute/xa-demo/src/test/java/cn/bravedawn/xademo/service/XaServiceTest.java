package cn.bravedawn.xademo.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : depers
 * @program : xa-demo
 * @description: 测试
 * @date : Created in 2021/9/10 22:56
 */
@SpringBootTest
public class XaServiceTest {

    @Autowired
    private XaService xaService;

    @Test
    public void testInsert() throws InterruptedException {
        xaService.testXa();

    }
}
