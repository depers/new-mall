package cn.bravedawn;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author 冯晓
 * @Date 2020/1/4 11:38
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:8080");
        config.addAllowedOrigin("http://shop.bravedawn.cn:8080");
        config.addAllowedOrigin("http://center.bavedawn.cn:8080");
        config.addAllowedOrigin("http://shop.bravedawn.cn");
        config.addAllowedOrigin("http://center.bavedawn.cn");
        // 设置sso跨域域名
        config.addAllowedOrigin("http://www.mtv.com");
        config.addAllowedOrigin("http://www.mtv.com:8080");
        config.addAllowedOrigin("http://www.music.com");
        config.addAllowedOrigin("http://www.music.com:8080");

        // 设置是否发送cookie信息
        config.setAllowCredentials(true);

        // 设置允许请求的方式
        config.addAllowedMethod("*");

        // 设置允许的header
        config.addAllowedHeader("*");

        // 2. 为url添加映射路径
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        // 为所有的请求路径设置跨域配置
        corsSource.registerCorsConfiguration("/**", config);

        // 3. 返回重新定义好的corsSource
        return new CorsFilter(corsSource);
    }

}
