package cn.bravedawn.mapper;

import cn.bravedawn.vo.ItemCommentVO;
import cn.bravedawn.vo.SearchItemsVO;
import cn.bravedawn.vo.ShopCartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author 冯晓
 * @Date 2020/1/13 22:15
 */
public interface ItemsMapperCustom {

    /**
     * 查询商品的用户评价
     * @param map
     * @return
     */
    List<ItemCommentVO> queryItemComments(@Param("paramsMap") Map<String, Object> map);

    /**
     * 商品搜索
     * @param map
     * @return
     */
    List<SearchItemsVO> searchItems(@Param("paramsMap") Map<String, Object> map);

    /**
     * 通过分类id搜索商品列表
     * @param map
     * @return
     */
    List<SearchItemsVO> searchItemsByThirdCat(@Param("paramsMap") Map<String, Object> map);

    /**
     * 根据规格ids查询最新的购物车中商品数据（用于刷新渲染购物车中的商品数据）
     * @param specIdsList
     * @return
     */
    List<ShopCartVO> queryItemsBySpecIds(@Param("paramsList") List specIdsList);

    /**
     * 减少库存
     * @param specId
     * @param pendingCounts
     * @return
     */
    int decreaseItemSpecStock(@Param("specId") String specId,
                              @Param("pendingCounts") int pendingCounts);
}
