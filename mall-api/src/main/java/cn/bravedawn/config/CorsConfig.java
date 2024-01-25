package cn.bravedawn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author : depers
 * @description :
 * @program : new-mall
 * @date : Created in 2024/1/8 22:37
 */

@Configuration
public class CorsConfig {

    /**
     * 后端跨域配置
     * 参考文章：https://www.jianshu.com/p/b07c74a2d8e0
     */


    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://shop.z.mukewang.com:8080");
        config.addAllowedOrigin("http://center.z.mukewang.com:8080");
        config.addAllowedOrigin("http://shop.z.mukewang.com");
        config.addAllowedOrigin("http://center.z.mukewang.com");

        // 响应头指定了该响应的资源是否被允许与给定的来源（origin）共享
        config.addAllowedOrigin("*");
        // 返回的响应是否会暴露验证信息给前端，在本项目中就是设置是否发送cookie信息
        config.setAllowCredentials(true);
        // 返回的响应中返回服务器支持的http请求方法，设置允许请求的方式
        config.addAllowedMethod("*");
        // 返回的响应中返回服务器允许客户端携带的头信息，设置允许的header
        config.addAllowedHeader("*");

        // 2.为CORS添加url映射路径，所有请求都设置cors添加相关配置信息
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);

        // 3. 返回重新定义好的corsSource
        return new CorsFilter(corsSource);
    }

}
