package cn.bravedawn.controller.center;


import cn.bravedawn.bo.center.OrderItemsCommentBO;
import cn.bravedawn.controller.BaseController;
import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.dto.PagedGridResult;
import cn.bravedawn.enums.YesOrNo;
import cn.bravedawn.pojo.OrderItems;
import cn.bravedawn.pojo.Orders;
import cn.bravedawn.service.center.MyCommentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "用户中心-评价模块", description = "用户中心评价模块相关接口")
@RestController
@RequestMapping("mycomments")
public class MyCommentsController extends BaseController {

    @Autowired
    private MyCommentsService myCommentsService;

    @Operation(summary = "查询订单列表", description = "查询订单列表")
    @PostMapping("/pending")
    public JsonResult pending(
            @Parameter(description = "用户id", required = true)
            @RequestParam String userId,
            @Parameter(description = "订单id", required = true)
            @RequestParam String orderId) {

        // 判断用户和订单是否关联
        JsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }
        // 判断该笔订单是否已经评价过，评价过了就不再继续
        Orders myOrder = (Orders)checkResult.getData();
        if (myOrder.getIsComment() == YesOrNo.YES.type) {
            return JsonResult.errorMsg("该笔订单已经评价");
        }

        List<OrderItems> list = myCommentsService.queryPendingComment(orderId);

        return JsonResult.ok(list);
    }


    @Operation(summary = "保存评论列表", description = "保存评论列表")
    @PostMapping("/saveList")
    public JsonResult saveList(
            @Parameter(description = "用户id", required = true)
            @RequestParam String userId,
            @Parameter(description = "订单id", required = true)
            @RequestParam String orderId,
            @RequestBody List<OrderItemsCommentBO> commentList) {

        System.out.println(commentList);

        // 判断用户和订单是否关联
        JsonResult checkResult = checkUserOrder(userId, orderId);
        if (checkResult.getStatus() != HttpStatus.OK.value()) {
            return checkResult;
        }
        // 判断评论内容list不能为空
        if (commentList == null || commentList.isEmpty() || commentList.size() == 0) {
            return JsonResult.errorMsg("评论内容不能为空！");
        }

        myCommentsService.saveComments(orderId, userId, commentList);
        return JsonResult.ok();
    }

    @Operation(summary = "查询我的评价", description = "查询我的评价")
    @PostMapping("/query")
    public JsonResult query(
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

        PagedGridResult grid = myCommentsService.queryMyComments(userId,
                page,
                pageSize);

        return JsonResult.ok(grid);
    }

}
