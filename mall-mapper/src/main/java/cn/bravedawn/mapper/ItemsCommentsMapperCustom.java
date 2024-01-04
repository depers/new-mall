package cn.bravedawn.mapper;

import cn.bravedawn.my.mapper.MyMapper;
import cn.bravedawn.pojo.ItemsComments;
import cn.bravedawn.vo.MyCommentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author 冯晓
 * @Date 2020/3/28 15:48
 */
public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    void saveComments(Map<String, Object> map);

    List<MyCommentVO> queryMyComments(@Param("paramsMap") Map<String, Object> map);
}
