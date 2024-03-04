package cn.bravedawn.xademo.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import javax.transaction.UserTransaction;
import java.io.IOException;

/**
 * @author : depers
 * @program : xa-demo
 * @description: 139数据源配置
 * @date : Created in 2021/9/10 21:52
 */
@Configuration
@MapperScan(value = "cn.bravedawn.xademo.dao.db140", sqlSessionFactoryRef = "sqlSessionFactoryBean140")
public class ConfigDB140 {

    /**
     * 声明139数据源
     * @return
     */
    @Bean("db140")
    public DataSource db139() {
        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setUser("root");
        xaDataSource.setPassword("fx1212");
        xaDataSource.setUrl("jdbc:mysql://192.168.156.140:3306/xa-140?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=UTF-8");

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(xaDataSource);
        atomikosDataSourceBean.setUniqueResourceName("db140");
        return atomikosDataSourceBean;
    }

    /**
     * 配置139 mybatis mapper location
     * @param dataSource
     * @return
     * @throws IOException
     */
    @Bean("sqlSessionFactoryBean140")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("db140") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("mapper/db140/*.xml"));
        return sqlSessionFactoryBean;
    }

}
