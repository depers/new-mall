package cn.bravedawn.rabbit.producer.config.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 数据库表自动初始化配置
 * @date : Created in 2021/3/1 22:09
 */
@Configuration
public class DataInitializerConfiguration {

    private final static Logger log = LoggerFactory.getLogger(DataInitializerConfiguration.class);

    @Autowired
    private DataSource datasource;

    @Value("classpath:rabbit-producer-message-schema.sql")
    private Resource schemaScript;


    @Bean
    public DataSourceInitializer initializer(){
        log.info("---------------datasource: {}", datasource);
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(datasource);
        initializer.setDatabaseCleaner(databasePopulator());
        return initializer;
    }


    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(schemaScript);
        return populator;
    }
}
