package cn.bravedawn.controller;

import cn.bravedawn.dto.JsonResult;
import cn.bravedawn.enums.YesOrNo;
import cn.bravedawn.pojo.Carousel;
import cn.bravedawn.pojo.Category;
import cn.bravedawn.service.CarouselService;
import cn.bravedawn.service.CategoryService;
import cn.bravedawn.vo.CategoryVO;
import cn.bravedawn.vo.NewItemsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "首页", description = "首页展示的相关接口")
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "获取首页轮播图列表", description = "获取首页轮播图列表", method = "GET")
    @GetMapping("/carousel")
    public JsonResult carousel() {
        List<Carousel> list = carouselService.queryAll(YesOrNo.YES.type);
        return JsonResult.ok(list);
    }


    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类，渲染展示到首页
     * 2. 如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */
    @Operation(summary = "获取商品分类(一级分类)", description = "获取商品分类(一级分类)", method = "GET")
    @GetMapping("/cats")
    public JsonResult cats() {
        List<Category> list = categoryService.queryAllRootLevelCat();
        return JsonResult.ok(list);
    }


    @Operation(summary = "获取商品子分类", description = "获取商品子分类", method = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JsonResult subCat(
            @Parameter(name = "rootCatId", description = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return JsonResult.errorMsg("分类不存在");
        }

        List<CategoryVO> list = categoryService.getSubCatList(rootCatId);
        return JsonResult.ok(list);
    }



    @Operation(summary = "查询每个一级分类下的最新6条商品数据", description = "查询每个一级分类下的最新6条商品数据", method = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JsonResult sixNewItems(
            @Parameter(name = "rootCatId", description = "一级分类id", required = true)
            @PathVariable Integer rootCatId) {

        if (rootCatId == null) {
            return JsonResult.errorMsg("分类不存在");
        }

        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);
        return JsonResult.ok(list);
    }

}
