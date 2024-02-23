package cn.bravedawn.controller;

import cn.bravedawn.bo.ShopCartBO;
import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.utils.JsonUtils;
import cn.bravedawn.utils.RedisOperator;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 购物车的数据是建立在redis上的
 */


@Tag(name = "购物车接口controller", description = "购物车接口相关的api")
@RequestMapping("shopcart")
@RestController
@Slf4j
public class ShopCatController extends BaseController {

    @Autowired
    private RedisOperator redisOperator;

    @Operation(summary = "添加商品到购物车", description = "添加商品到购物车")
    @PostMapping("/add")
    public JsonResult add(
            @RequestParam String userId,
            @RequestBody ShopCartBO shopcartBO,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg("请求参数错误");
        }

        log.info("商品信息：{}", shopcartBO);

        // 1. 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存
        // 2. 需要判断当前购物车中是否包含已经存在的商品，如果存在则累加购买数量
        String shopCartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        List<ShopCartBO> shopcartList = null;
        if (StringUtils.isNotBlank(shopCartJson)){
            // redis中已经有购物车的数据
            shopcartList = JsonUtils.jsonToList(shopCartJson, ShopCartBO.class);

            // 判断购物车中是否存在已有的商品，如果有的话counts累加
            boolean isHaving = false;
            for (ShopCartBO sc : shopcartList){
                String tmpSpecId = sc.getSpecId();
                if (tmpSpecId.equals(shopcartBO.getSpecId())){
                    sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
                    isHaving = true;
                }
            }
            if (!isHaving) {
                shopcartList.add(shopcartBO);
            }
        } else{
            // redis中没有购物车
            shopcartList = Lists.newArrayList();
            // 直接添加到购物车
            shopcartList.add(shopcartBO);
        }

        // 覆盖现有redis中的购物车
        redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));
        return JsonResult.ok();
    }

    @Operation(summary = "从购物车中删除商品", description = "从购物车中删除商品")
    @PostMapping("/del")
    public JsonResult del(
            @RequestParam String userId,
            @RequestParam String itemSpecId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
            return JsonResult.errorMsg("参数不能为空");
        }

        // TODO 用户在页面删除购物车中的商品数据，如果此时用户已经登录，则需要同步删除后端购物车中的商品

        return JsonResult.ok();
    }

}
