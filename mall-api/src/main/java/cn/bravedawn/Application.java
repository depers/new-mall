package cn.bravedawn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2023-12-28 10:58
 */

@SpringBootApplication(exclude = {RedisAutoConfiguration.class, SecurityAutoConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
