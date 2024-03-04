package cn.bravedawn.tccdemo.dao.db139;

import cn.bravedawn.tccdemo.model.db139.AccountA;
import cn.bravedawn.tccdemo.model.db139.AccountAExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountAMapper {
    long countByExample(AccountAExample example);

    int deleteByExample(AccountAExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AccountA record);

    int insertSelective(AccountA record);

    List<AccountA> selectByExample(AccountAExample example);

    AccountA selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AccountA record, @Param("example") AccountAExample example);

    int updateByExample(@Param("record") AccountA record, @Param("example") AccountAExample example);

    int updateByPrimaryKeySelective(AccountA record);

    int updateByPrimaryKey(AccountA record);
}