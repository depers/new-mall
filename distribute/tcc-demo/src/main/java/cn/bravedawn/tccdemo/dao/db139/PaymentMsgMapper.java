package cn.bravedawn.tccdemo.dao.db139;

import cn.bravedawn.tccdemo.model.db139.PaymentMsg;
import cn.bravedawn.tccdemo.model.db139.PaymentMsgExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PaymentMsgMapper {
    long countByExample(PaymentMsgExample example);

    int deleteByExample(PaymentMsgExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PaymentMsg record);

    int insertSelective(PaymentMsg record);

    List<PaymentMsg> selectByExample(PaymentMsgExample example);

    PaymentMsg selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PaymentMsg record, @Param("example") PaymentMsgExample example);

    int updateByExample(@Param("record") PaymentMsg record, @Param("example") PaymentMsgExample example);

    int updateByPrimaryKeySelective(PaymentMsg record);

    int updateByPrimaryKey(PaymentMsg record);
}