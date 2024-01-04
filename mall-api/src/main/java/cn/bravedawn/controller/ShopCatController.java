package cn.bravedawn.controller;

import cn.bravedawn.bo.ShopCartBO;
import cn.bravedawn.dto.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 购物车的数据是建立在redis上的
 */


@Api(value = "购物车接口controller", tags = {"购物车接口相关的api"})
@RequestMapping("shopcart")
@RestController
@Slf4j
public class ShopCatController {

    @ApiOperation(value = "添加商品到购物车", notes = "添加商品到购物车", httpMethod = "POST")
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

        // TODO 前端用户在登录的情况下，添加商品到购物车，会同时在后端同步购物车到redis缓存

        return JsonResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
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
