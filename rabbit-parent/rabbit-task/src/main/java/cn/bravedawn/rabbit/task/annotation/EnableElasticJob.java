package cn.bravedawn.rabbit.task.annotation;

import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 启用elasticJob
 * @date : Created in 2021/3/14 17:48
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
@Import(ElasticsearchAutoConfiguration.class)
public @interface EnableElasticJob {
}
