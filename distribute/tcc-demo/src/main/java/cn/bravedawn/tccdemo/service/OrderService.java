package cn.bravedawn.tccdemo.service;

import cn.bravedawn.tccdemo.dao.db140.OrdersMapper;
import cn.bravedawn.tccdemo.model.db140.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author : depers
 * @program : tcc-demo
 * @description: 订单service
 * @date : Created in 2021/9/24 13:19
 */
@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    /**
     * 订单回调接口
     * @param orderId
     * @return 0：支付成功
     *         1：订单不存在
     */
    @Transactional(transactionManager = "tm140")
    public int handleOrder(int orderId) {
        log.info("订单回调接口, orderId={}.", orderId);
        Orders orders = ordersMapper.selectByPrimaryKey(orderId);
        if (orders == null) {
            return 1;
        }
        orders.setOrderStatus(1); // 设置为已支付
        orders.setUpdateTime(new Date());
        orders.setUpdateUser(0); // 系统更新
        ordersMapper.updateByPrimaryKey(orders);
        return 0;
    }




}
