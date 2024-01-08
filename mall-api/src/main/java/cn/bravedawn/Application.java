package cn.bravedawn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2023-12-28 10:58
 */

@SpringBootApplication
@MapperScan(basePackages = "cn.bravedawn.mapper")                       // 配置Mybatis扫描包的路径
@ComponentScan(basePackages = {"cn.bravedawn", "org.n3r.idworker"})     // 配置Spring容器扫描的路径
@EnableScheduling                                                       // 开启定时任务
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
