package cn.bravedawn.tccdemo.config;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * @author : depers
 * @program : tcc-demo
 * @description: 139数据源和事务管理器配置
 * @date : Created in 2021/9/17 7:12
 */
@Configuration
@MapperScan(value = "cn.bravedawn.tccdemo.dao.db140", sqlSessionFactoryRef = "factoryBean140")
public class Db140Config {

    @Bean("db140")
    public DataSource db140() {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("fx1212");
        dataSource.setUrl("jdbc:mysql://192.168.156.140:3306/tcc-140?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=UTF-8");
        return dataSource;
    }

    @Bean("factoryBean140")
    public SqlSessionFactoryBean factoryBean(@Qualifier("db140") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("mapper/db140/*.xml"));
        return sqlSessionFactoryBean;
    }

    @Bean("tm140")
    public PlatformTransactionManager transactionManager(@Qualifier("db140") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

}
