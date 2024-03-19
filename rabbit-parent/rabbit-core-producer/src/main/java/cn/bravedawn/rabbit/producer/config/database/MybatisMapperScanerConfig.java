package cn.bravedawn.rabbit.producer.config.database;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: mapper扫描配置
 * @date : Created in 2021/3/2 15:57
 */
@Configuration
@AutoConfigureAfter(DataSourceConfiguration.class)
public class MybatisMapperScanerConfig {


    @Bean(name = "rabbitMybatisMapperScanerConfig")
    public MapperScannerConfigurer rabbitMybatisMapperScanerConfig() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        mapperScannerConfigurer.setSqlSessionFactoryBeanName("rabbitSqlSessionFactory");
        mapperScannerConfigurer.setBasePackage("cn.bravedawn.rabbit.producer.mapper");
        return mapperScannerConfigurer;
    }
}
