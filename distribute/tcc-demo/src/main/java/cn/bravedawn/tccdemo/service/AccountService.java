package cn.bravedawn.tccdemo.service;

import cn.bravedawn.tccdemo.dao.db139.AccountAMapper;
import cn.bravedawn.tccdemo.dao.db140.AccountBMapper;
import cn.bravedawn.tccdemo.model.db139.AccountA;
import cn.bravedawn.tccdemo.model.db140.AccountB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @author : depers
 * @program : tcc-demo
 * @description: 转账Service
 * @date : Created in 2021/9/17 7:07
 */
@Service
public class AccountService {


    @Autowired
    private AccountAMapper accountAMapper;

    @Autowired
    private AccountBMapper accountBMapper;


    /**
     * 下面这段程序模拟了两家银行的两个账户之间的转账操作，账户A向账户B转账200元，初始时他两账户各1000元
     * 账户A余额：800
     * 账户B余额：1200
     */
    @Transactional(transactionManager = "tm139")
    public void transferAccount() {
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(200)));
        accountAMapper.updateByPrimaryKey(accountA);

        AccountB accountB = accountBMapper.selectByPrimaryKey(2);
        accountB.setBalance(accountB.getBalance().add(new BigDecimal(200)));
        accountBMapper.updateByPrimaryKey(accountB);

    }


    /**
     * 由于使用的是139数据库的事务管理器，所以139的数据报错之后会回滚
     * 账户A余额：1000
     * 账户B余额：1000
     */
    @Transactional(transactionManager = "tm139")
    public void transferAccountTwo() {
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(200)));
        accountAMapper.updateByPrimaryKey(accountA);

        int i = 1/0;

        AccountB accountB = accountBMapper.selectByPrimaryKey(2);
        accountB.setBalance(accountB.getBalance().add(new BigDecimal(200)));
        accountBMapper.updateByPrimaryKey(accountB);

    }

    /**
     * 由于使用的是139数据库的事务管理器，所以139的数据报错之后会回滚，但是140的数据却不会回滚
     * 账户A余额：1000
     * 账户B余额：1200
     */
    @Transactional(transactionManager = "tm139")
    public void transferAccountThree() {
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(200)));
        accountAMapper.updateByPrimaryKey(accountA);

        AccountB accountB = accountBMapper.selectByPrimaryKey(2);
        accountB.setBalance(accountB.getBalance().add(new BigDecimal(200)));
        accountBMapper.updateByPrimaryKey(accountB);
        int i = 1/0;

    }


    /**
     * 由于使用的是139数据库的事务管理器，所以139的数据报错之后会回滚，但是140的数据却不会回滚，需要我们手动去做补偿
     * 账户A余额：1000
     * 账户B余额：1000
     */
    @Transactional(transactionManager = "tm139")
    public void transferAccountFour() {
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(200)));
        accountAMapper.updateByPrimaryKey(accountA);

        try {
            AccountB accountB = accountBMapper.selectByPrimaryKey(2);
            accountB.setBalance(accountB.getBalance().add(new BigDecimal(200)));
            accountBMapper.updateByPrimaryKey(accountB);
            int i = 1/0;
        } catch (Exception e) {
            AccountB accountB = accountBMapper.selectByPrimaryKey(2);
            accountB.setBalance(accountB.getBalance().subtract(new BigDecimal(200)));
            accountBMapper.updateByPrimaryKey(accountB);

            // 为了让139的数据回滚，需要把错继续抛出
            throw e;
        }


    }

}
