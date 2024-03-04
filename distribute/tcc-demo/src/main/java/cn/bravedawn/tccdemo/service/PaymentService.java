package cn.bravedawn.tccdemo.service;

import cn.bravedawn.tccdemo.dao.db139.AccountAMapper;
import cn.bravedawn.tccdemo.dao.db139.PaymentMsgMapper;
import cn.bravedawn.tccdemo.dao.db140.AccountBMapper;
import cn.bravedawn.tccdemo.model.db139.AccountA;
import cn.bravedawn.tccdemo.model.db139.PaymentMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author : depers
 * @program : tcc-demo
 * @description: 支付逻辑
 * @date : Created in 2021/9/24 7:18
 */
@Service
@Slf4j
public class PaymentService {


    @Autowired
    private AccountAMapper accountAMapper;

    @Autowired
    private AccountBMapper accountBMapper;

    @Autowired
    private PaymentMsgMapper paymentMsgMapper;

    @Autowired
    private DefaultMQProducer defaultMQProducer;


    /**
     * 支付接口
     * @param userId
     * @param orderId
     * @param amount
     * @return 0：支付成功
     *         1：账户不存在
     *         2：余额不足
     */
    // 这里一定要指定Throwable吗
    @Transactional(transactionManager = "tm139", rollbackFor = Throwable.class)
    public int payment(int userId, int orderId, BigDecimal amount) {
        log.info("订单支付接口, userId={}, orderId={}, amount={}.", userId, orderId, amount);

        // 支付操作
        AccountA accountA = accountAMapper.selectByPrimaryKey(userId);
        if (accountA == null) {
            return 1;
        }
        if (accountA.getBalance().compareTo(amount) < 0) {
            return 2;
        }
        accountA.setBalance(accountA.getBalance().subtract(amount));
        accountAMapper.updateByPrimaryKey(accountA);

        // 保存本地消息
        PaymentMsg paymentMsg = new PaymentMsg();
        paymentMsg.setOrderId(orderId);
        paymentMsg.setStatus(0);
        paymentMsg.setFailureCnt(0);
        paymentMsg.setCreateTime(new Date());
        paymentMsg.setCreateUser(userId);
        paymentMsg.setUpdateTime(new Date());
        paymentMsg.setUpdateUser(userId);
        paymentMsgMapper.insert(paymentMsg);

        return 0;
    }


    /**
     * 支付接口（消息队列）
     * @param userId
     * @param orderId
     * @param amount
     * @return 0：支付成功
     *         1：账户不存在
     *         2：余额不足
     */
    @Transactional(transactionManager = "tm139", rollbackFor = Throwable.class)
    public int paymentOfMQ(int userId, int orderId, BigDecimal amount) throws Exception {
        log.info("消息队列订单支付接口, userId={}, orderId={}, amount={}.", userId, orderId, amount);

        // 支付操作
        AccountA accountA = accountAMapper.selectByPrimaryKey(userId);
        if (accountA == null) {
            return 1;
        }
        if (accountA.getBalance().compareTo(amount) < 0) {
            return 2;
        }
        accountA.setBalance(accountA.getBalance().subtract(amount));
        accountAMapper.updateByPrimaryKey(accountA);

        // 生产消息
        Message message = new Message();
        message.setTopic("payment");
        message.setKeys(orderId+"");
        message.setBody("订单已支付".getBytes());

        try {
            SendResult result = defaultMQProducer.send(message);
            if (result.getSendStatus() == SendStatus.SEND_OK){
                return 0;
            }else {
                throw new Exception("消息发送失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Transactional(transactionManager = "tm139")
    public void testTransactionalByRuntimeException() {
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(100)));
        accountAMapper.updateByPrimaryKey(accountA);

        int i = 1 / 0;
    }


    @Transactional(transactionManager = "tm139")
    public void testTransactionalByException() throws Exception {
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(100)));
        accountAMapper.updateByPrimaryKey(accountA);
        throw new Exception();

    }

    @Transactional(transactionManager = "tm139")
    public void testTransactionalByCatchThrowRunTimeException() throws Exception {
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(100)));
        accountAMapper.updateByPrimaryKey(accountA);

        try {
            int i = 1/0;
        } catch (Throwable e) {
            throw new RuntimeException();
        }

    }


    @Transactional(transactionManager = "tm139")
    public void testTransactionalByCatchThrowException() throws Exception {
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(100)));
        accountAMapper.updateByPrimaryKey(accountA);

        try {
            int i = 1/0;
        } catch (Throwable e) {
            throw new Exception();
        }

    }

    @Transactional(transactionManager = "tm139")
    public void testTransactionalByError() throws Exception {
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(100)));
        accountAMapper.updateByPrimaryKey(accountA);
        throw new Error();
    }

    @Transactional(transactionManager = "tm139", rollbackFor = Exception.class)
    public void testTransactionalByRollBackFor() throws Exception {
        AccountA accountA = accountAMapper.selectByPrimaryKey(1);
        accountA.setBalance(accountA.getBalance().subtract(new BigDecimal(100)));
        accountAMapper.updateByPrimaryKey(accountA);
        throw new Exception();

    }


}
