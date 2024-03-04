package cn.bravedawn.tccdemo.controller;

import cn.bravedawn.tccdemo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : depers
 * @program : tcc-demo
 * @description: 订单controller
 * @date : Created in 2021/9/24 13:31
 */
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;


    @PostMapping("handleOrder")
    public String handlerOrder(int orderId) {
        try {
            int result = orderService.handleOrder(orderId);

            if (result == 0) {
                return "success";
            } else {
                return "fail";
            }
        } catch (Throwable e) {
            return "fail";
        }
    }
}
