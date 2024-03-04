package cn.bravedawn.tccdemo.controller;

import cn.bravedawn.tccdemo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author : depers
 * @program : tcc-demo
 * @description: 支付controller
 * @date : Created in 2021/9/24 7:25
 */
@RestController
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping("payment")
    public String payment(int userId, int orderId, BigDecimal amount) {
        int result = paymentService.payment(userId, orderId, amount);
        return "支付结果：" + result;
    }

    @RequestMapping("payment_mq")
    public String paymentOfMQ(int userId, int orderId, BigDecimal amount) throws Exception {
        int result = paymentService.paymentOfMQ(userId, orderId, amount);
        return "支付结果：" + result;
    }
}
