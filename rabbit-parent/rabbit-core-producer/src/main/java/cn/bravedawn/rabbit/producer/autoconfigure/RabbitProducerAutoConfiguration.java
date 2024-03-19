package cn.bravedawn.rabbit.producer.autoconfigure;

import cn.bravedawn.rabbit.task.annotation.EnableElasticJob;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 自动装配的类
 * @date : Created in 2021/2/25 20:56
 */

@EnableElasticJob
@Configuration
@ComponentScan("cn.bravedawn.rabbit.producer.*")
public class RabbitProducerAutoConfiguration {


}
