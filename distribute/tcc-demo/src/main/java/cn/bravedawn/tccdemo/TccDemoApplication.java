package cn.bravedawn.tccdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author : depers
 * @program : tcc-demo
 * @description: app
 * @date : Created in 2021/9/17 6:48
 */
@SpringBootApplication
@EnableScheduling
public class TccDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TccDemoApplication.class, args);
    }
}
