package cn.bravedawn.controller;

import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.pojo.Orders;
import cn.bravedawn.service.center.MyOrdersService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-03 15:51
 */
public class BaseController {

    @Autowired
    public MyOrdersService myOrdersService;

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOODIE_SHOPCART = "shopcart";


    /**
     * 用于验证用户和订单是否有关联关系，避免非法用户调用
     * @return
     */
    public JsonResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return JsonResult.errorMsg("订单不存在！");
        }
        return JsonResult.ok(order);
    }

}
