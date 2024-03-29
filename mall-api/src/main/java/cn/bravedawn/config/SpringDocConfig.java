package cn.bravedawn.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : depers
 * @program : new-mall
 * @date : Created in 2024/1/9 23:38
 */
@Configuration
public class SpringDocConfig {


    /**
     * 前端访问地址：http://localhost:8080/swagger-ui/index.html#/
     */

    @Bean
    public OpenAPI restfulOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring Boot3 Restful Zoo API")
                        .description("Zoo & Animal Detail APi")
                        .version("v0.0.1")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringDoc Wiki Documentation")
                        .url("https://springdoc.org/v2"));
    }
}
