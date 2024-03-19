package cn.bravedawn.rabbit.producer.config.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 数据源配置
 * @date : Created in 2021/3/1 21:58
 */
@Configuration
@PropertySource("classpath:rabbit-producer-message.properties")
public class DataSourceConfiguration {

    private final static Logger log = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Value("${rabbit.producer.druid.type}")
    private Class<? extends DataSource> dataSourceType;

    @Bean(name = "datasource")
    @Primary
    @ConfigurationProperties(prefix = "rabbit.producer.druid.jdbc")
    public DataSource dataSource() {
        DataSource dataSource = DataSourceBuilder.create().type(dataSourceType).build();
        log.info("============= rabbitProducerDataSource : {} ================", dataSource);
        return dataSource;
    }

    public DataSourceProperties primaryDataSourceProperties(){
        return new DataSourceProperties();
    }

    public DataSource primaryDataSource(){
        return primaryDataSourceProperties().initializeDataSourceBuilder().build();
    }

}
