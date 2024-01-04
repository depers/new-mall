package cn.bravedawn.service;

import cn.bravedawn.pojo.Category;
import cn.bravedawn.vo.CategoryVO;
import cn.bravedawn.vo.NewItemsVO;

import java.util.List;

/**
 * @Author : fengx9
 * @Project : new-mall
 * @Date : Created in 2024-01-03 15:22
 */
public interface CategoryService {


    /**
     * 查询所有一级分类
     * @return
     */
    public List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类id查询子分类信息
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询首页每个一级分类下的6条最新商品数据
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);
}
