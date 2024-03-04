package cn.bravedawn.tccdemo;

import cn.bravedawn.tccdemo.service.AccountService;
import cn.bravedawn.tccdemo.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : depers
 * @program : tcc-demo
 * @description: 转账测试
 * @date : Created in 2021/9/18 21:29
 */


@SpringBootTest
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private PaymentService paymentService;

    @Test
    public void testTransferAccount() {
        // accountService.transferAccount();
        // accountService.transferAccountTwo();
        // accountService.transferAccountThree();
        accountService.transferAccountFour();
    }

    @Test
    public void testTransaction() throws Exception {
        // paymentService.testTransactionalByRuntimeException(); // 会回滚
        // paymentService.testTransactionalByException();        // 不会回滚
        // paymentService.testTransactionalByCatchThrowRunTimeException(); // 会回滚
        // paymentService.testTransactionalByCatchThrowException(); // 不会回滚
        // paymentService.testTransactionalByError(); // 会回滚
        paymentService.testTransactionalByRollBackFor();

    }


}
