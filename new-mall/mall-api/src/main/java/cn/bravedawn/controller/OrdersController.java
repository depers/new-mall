package cn.bravedawn.controller;


import cn.bravedawn.bo.ShopCartBO;
import cn.bravedawn.bo.SubmitOrderBO;
import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.enums.OrderStatusEnum;
import cn.bravedawn.enums.PayMethod;
import cn.bravedawn.pojo.OrderStatus;
import cn.bravedawn.property.PaymentProperty;
import cn.bravedawn.service.OrderService;
import cn.bravedawn.utils.CookieUtils;
import cn.bravedawn.utils.JsonUtils;
import cn.bravedawn.utils.RedisOperator;
import cn.bravedawn.vo.MerchantOrdersVO;
import cn.bravedawn.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Tag(name = "订单相关", description = "订单相关的api接口")
@RequestMapping("orders")
@RestController
public class OrdersController extends BaseController {

    final static Logger logger = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PaymentProperty paymentProperty;

    @Autowired
    private RedisOperator redisOperator;

    @Operation(summary = "用户下单", description = "用户下单")
    @PostMapping("/create")
    public JsonResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
            && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type ) {
            return JsonResult.errorMsg("支付方式不支持！");
        }

        // 查询购物车
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());
        if (StringUtils.isBlank(shopCartJson)){
            return JsonResult.errorMsg("购物车数据不正确");
        }
        List<ShopCartBO> shopcartList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);

        // 1. 创建订单
        OrderVO orderVO = orderService.createOrder(shopcartList, submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        /**
         * 1001
         * 2002 -> 用户购买
         * 3003 -> 用户购买
         * 4004
         */
        // 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        // 清理覆盖现有的redis汇总的购物数据
        shopcartList.removeAll(orderVO.getToBeRemovedShopcatdList());
        redisOperator.set(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId(), JsonUtils.objectToJson(shopcartList));
        // 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartList), true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(paymentProperty.getPayReturnUrl());

        // 为了方便测试购买，所以所有的支付金额都统一改为1分钱
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","imooc");
        headers.add("password","imooc");

        HttpEntity<MerchantOrdersVO> entity =
                new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<JsonResult> responseEntity =
                restTemplate.postForEntity(paymentProperty.getUrl(),
                                            entity,
                                            JsonResult.class);
        JsonResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            logger.error("发送错误：{}", paymentResult.getMsg());
            return JsonResult.errorMsg("支付中心订单创建失败，请联系管理员！");
        }

        return JsonResult.ok(orderId);
    }


    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }


    @PostMapping("getPaidOrderInfo")
    public JsonResult getPaidOrderInfo(String orderId) {

        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JsonResult.ok(orderStatus);
    }
}
