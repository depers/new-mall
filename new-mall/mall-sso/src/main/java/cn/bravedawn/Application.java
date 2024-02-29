package cn.bravedawn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author : depers
 * @program : new-mall
 * @date : Created in 2024/2/29 16:34
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@MapperScan("cn.bravedawn.mapper")
@ComponentScan(basePackages = {"cn.bravedawn", "org.n3r.idworker"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}