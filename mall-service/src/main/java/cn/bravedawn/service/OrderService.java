package cn.bravedawn.service;

import cn.bravedawn.bo.SubmitOrderBO;
import cn.bravedawn.pojo.OrderStatus;
import cn.bravedawn.vo.OrderVO;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-04 16:34
 */
public interface OrderService {

    /**
     * 用于创建订单相关信息
     * @param submitOrderBO
     */
    public OrderVO createOrder(SubmitOrderBO submitOrderBO);

    /**
     * 修改订单状态
     * @param orderId
     * @param orderStatus
     */
    public void updateOrderStatus(String orderId, Integer orderStatus);

    /**
     * 查询订单状态
     * @param orderId
     * @return
     */
    public OrderStatus queryOrderStatusInfo(String orderId);

    /**
     * 关闭超时未支付订单
     */
    public void closeOrder();
}
