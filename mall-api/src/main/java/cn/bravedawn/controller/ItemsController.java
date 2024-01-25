package cn.bravedawn.controller;

import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.dto.PagedGridResult;
import cn.bravedawn.pojo.Items;
import cn.bravedawn.pojo.ItemsImg;
import cn.bravedawn.pojo.ItemsSpec;
import cn.bravedawn.pojo.ItemsParam;
import cn.bravedawn.service.ItemService;
import cn.bravedawn.vo.CommentLevelCountsVO;
import cn.bravedawn.vo.ItemInfoVO;
import cn.bravedawn.vo.ShopCartVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "商品接口", description = "商品信息展示的相关接口")
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController {

    @Autowired
    private ItemService itemService;

    @Operation(summary = "查询商品详情", description = "查询商品详情", method = "GET")
    @GetMapping("/info/{itemId}")
    public JsonResult info(
            @Parameter(name = "itemId", description = "商品id", required = true)
            @PathVariable String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        Items item = itemService.queryItemById(itemId);
        List<ItemsImg> itemImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);

        return JsonResult.ok(itemInfoVO);
    }

    @Operation(summary = "查询商品评价等级", description = "查询商品评价等级", method = "GET")
    @GetMapping("/commentLevel")
    public JsonResult commentLevel(
            @Parameter(name = "itemId", description = "商品id", required = true)
            @RequestParam String itemId) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        CommentLevelCountsVO countsVO = itemService.queryCommentCounts(itemId);
        return JsonResult.ok(countsVO);
    }

    @Operation(summary = "查询商品评论", description = "查询商品评论", method = "GET")
    @GetMapping("/comments")
    public JsonResult comments(
            @Parameter(name = "itemId", description = "商品id", required = true)
            @RequestParam String itemId,
            @Parameter(name = "level", description = "评价等级", required = false)
            @RequestParam Integer level,
            @Parameter(name = "page", description = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @Parameter(name = "pageSize", description = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(itemId)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult grid = itemService.queryPagedComments(itemId,
                                                                level,
                                                                page,
                                                                pageSize);
        return JsonResult.ok(grid);
    }

    @Operation(summary = "搜索商品列表", description = "搜索商品列表", method = "GET")
    @GetMapping("/search")
    public JsonResult search(
            @Parameter(name = "keywords", description = "关键字", required = true)
            @RequestParam String keywords,
            @Parameter(name = "sort", description = "排序", required = false)
            @RequestParam String sort,
            @Parameter(name = "page", description = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @Parameter(name = "pageSize", description = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(keywords)) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult grid = itemService.searhItems(keywords,
                                                        sort,
                                                        page,
                                                        pageSize);

        return JsonResult.ok(grid);
    }

    @Operation(summary = "通过分类id搜索商品列表", description = "通过分类id搜索商品列表", method = "GET")
    @GetMapping("/catItems")
    public JsonResult catItems(
            @Parameter(name = "catId", description = "三级分类id", required = true)
            @RequestParam Integer catId,
            @Parameter(name = "sort", description = "排序", required = false)
            @RequestParam String sort,
            @Parameter(name = "page", description = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @Parameter(name = "pageSize", description = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (catId == null) {
            return JsonResult.errorMsg(null);
        }

        if (page == null) {
            page = 1;
        }

        if (pageSize == null) {
            pageSize = PAGE_SIZE;
        }

        PagedGridResult grid = itemService.searhItems(catId,
                sort,
                page,
                pageSize);

        return JsonResult.ok(grid);
    }

    // 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
    @Operation(summary = "根据商品规格ids查找最新的商品数据", description = "根据商品规格ids查找最新的商品数据", method = "GET")
    @GetMapping("/refresh")
    public JsonResult refresh(
            @Parameter(name = "itemSpecIds", description = "拼接的规格ids", required = true, example = "1001,1003,1005")
            @RequestParam String itemSpecIds) {

        if (StringUtils.isBlank(itemSpecIds)) {
            return JsonResult.ok();
        }

        List<ShopCartVO> list = itemService.queryItemsBySpecIds(itemSpecIds);
        return JsonResult.ok(list);
    }
}
