package cn.bravedawn.xademo.config;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.cj.jdbc.MysqlXADataSource;
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
@MapperScan(value = "cn.bravedawn.xademo.dao.db139", sqlSessionFactoryRef = "sqlSessionFactoryBean139")
public class ConfigDB139 {

    /**
     * 声明139数据源
     * @return
     */
    @Bean("db139")
    public DataSource db139() {
        MysqlXADataSource xaDataSource = new MysqlXADataSource();
        xaDataSource.setUser("root");
        xaDataSource.setPassword("fx1212");
        xaDataSource.setUrl("jdbc:mysql://192.168.156.139:3306/xa-139?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=UTF-8");

        AtomikosDataSourceBean atomikosDataSourceBean = new AtomikosDataSourceBean();
        atomikosDataSourceBean.setXaDataSource(xaDataSource);
        atomikosDataSourceBean.setUniqueResourceName("db139");
        return atomikosDataSourceBean;
    }

    /**
     * 配置139 mybatis mapper location
     * @param dataSource
     * @return
     * @throws IOException
     */
    @Bean("sqlSessionFactoryBean139")
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("db139") DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources("mapper/db139/*.xml"));
        return sqlSessionFactoryBean;
    }


    /**
     * 配置事务管理器 TM
     * @return
     */
    @Bean("xaTransaction")
    public JtaTransactionManager jtaTransactionManager() {
        UserTransaction userTransaction = new UserTransactionImp();
        UserTransactionManager userTransactionManager = new UserTransactionManager();

        return new JtaTransactionManager(userTransaction, userTransactionManager);
    }
}
