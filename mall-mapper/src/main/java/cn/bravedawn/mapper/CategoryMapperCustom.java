package cn.bravedawn.mapper;

import cn.bravedawn.vo.CategoryVO;
import cn.bravedawn.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author 冯晓
 * @Date 2020/1/6 15:54
 */
public interface CategoryMapperCustom {

    List<CategoryVO> getSubCatList(Integer rootCatId);

    List<NewItemsVO> getSixNewItemsLazy(@Param("paramsMap") Map<String, Object> map);
}
