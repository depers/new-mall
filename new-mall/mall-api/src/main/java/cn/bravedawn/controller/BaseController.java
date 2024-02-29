package cn.bravedawn.controller;

import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.pojo.Orders;
import cn.bravedawn.pojo.Users;
import cn.bravedawn.service.center.MyOrdersService;
import cn.bravedawn.utils.RedisOperator;
import cn.bravedawn.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.UUID;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-03 15:51
 */
@Controller
public class BaseController {

    @Autowired
    public MyOrdersService myOrdersService;

    @Autowired
    private RedisOperator redisOperator;

    public static final Integer COMMON_PAGE_SIZE = 10;
    public static final Integer PAGE_SIZE = 20;
    public static final String FOODIE_SHOPCART = "shopcart";
    public static final String REDIS_USER_TOKEN = "redis_user_token";


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

    /**
     * 生成用户token，存入redis会话
     * @param user
     * @return
     */
    public UserVO conventUser(Users user){
        String uniqueToken = UUID.randomUUID().toString().trim();
        redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), uniqueToken);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        userVO.setUserUniqueToken(uniqueToken);
        return userVO;
    }

}
