package cn.bravedawn.controller.center;


import cn.bravedawn.controller.BaseController;
import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.dto.PagedGridResult;
import cn.bravedawn.vo.OrderStatusCountsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "用户中心-我的订单", description = "用户中心我的订单相关接口")
@RestController
@RequestMapping("myorders")
public class MyOrdersController extends BaseController {



    @Operation(summary = "获得订单状态数概况", description = "获得订单状态数概况")
    @PostMapping("/statusCounts")
    public JsonResult statusCounts(
            @Parameter(description = "用户id", required = true)
            @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg(null);
        }

        OrderStatusCountsVO result = myOrdersService.getOrderStatusCounts(userId);

        return JsonResult.ok(result);
    }

    @Operation(summary = "查询订单列表", description = "查询订单列表")
    @PostMapping("/query")
    public JsonResult query(
            @Parameter(description = "用户id", required = true)
            @RequestParam String userId,
            @Parameter(description = "订单状态", required = false)
            @RequestParam Integer orderStatus,
            @Parameter(description= "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @Parameter(description= "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = myOrdersService.queryMyOrders(userId,
                                                            orderStatus,
                                                            page,
                                                            pageSize);

        return JsonResult.ok(grid);
    }


    // 商家发货没有后端，所以这个接口仅仅只是用于模拟
    @Operation(summary="商家发货", description="商家发货")
    @GetMapping("/deliver")
    public JsonResult deliver(
            @Parameter(description = "订单id", required = true)
            @RequestParam String orderId) throws Exception {

        if (StringUtils.isBlank(orderId)) {
            return JsonResult.errorMsg("订单ID不能为空");
        }
        myOrdersService.updateDeliverOrderStatus(orderId);
        return JsonResult.ok();
    }


    @Operation(summary="用户确认收货", description="用户确认收货")
    @PostMapping("/confirmReceive")
    public JsonResult confirmReceive(
            @Parameter(description = "订单id", required = true)
            @RequestParam String orderId,
            @Parameter(description = "用户id", required = true)
            @RequestParam String userId) throws Exception {

        JsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean res = myOrdersService.updateReceiveOrderStatus(orderId);
        if (!res) {
            return JsonResult.errorMsg("订单确认收货失败！");
        }

        return JsonResult.ok();
    }

    @Operation(summary="用户删除订单", description="用户删除订单")
    @PostMapping("/delete")
    public JsonResult delete(
            @Parameter(description = "订单id", required = true)
            @RequestParam String orderId,
            @Parameter(description = "用户id", required = true)
            @RequestParam String userId) throws Exception {

        JsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }

        boolean res = myOrdersService.deleteOrder(userId, orderId);
        if (!res) {
            return JsonResult.errorMsg("订单删除失败！");
        }

        return JsonResult.ok();
    }



    @Operation(summary = "查询订单动向", description = "查询订单动向")
    @PostMapping("/trend")
    public JsonResult trend(
            @Parameter(description = "用户id", required = true)
            @RequestParam String userId,
            @Parameter(description = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @Parameter(description = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return JsonResult.errorMsg(null);
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult grid = myOrdersService.getOrdersTrend(userId,
                page,
                pageSize);

        return JsonResult.ok(grid);
    }

}
