package cn.bravedawn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author : depers
 * @description :
 * @program : new-mall
 * @date : Created in 2024/1/8 22:48
 */

@Configuration
@EnableSwagger2
public class Swagger2 {

    // 配置swagger2核心配置 docket
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)          // 指定api类型为swagger2
                .apiInfo(apiInfo())                             // 用于定义api文档汇总信息
                .select()
                .apis(RequestHandlerSelectors
                        .basePackage("cn.bravedawn.controller"))   // 指定controller包
                .paths(PathSelectors.any())                     // 所有controller
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("天天吃货 电商平台接口api")                  // 文档页标题
                .contact(new Contact("imooc",
                        "https://www.imooc.com",
                        "abc@imooc.com"))                  // 联系人信息
                .description("专为天天吃货提供的api文档")           // 详细信息
                .version("1.0.1")   // 文档版本号
                .termsOfServiceUrl("https://www.imooc.com")     // 网站地址
                .build();
    }


}
