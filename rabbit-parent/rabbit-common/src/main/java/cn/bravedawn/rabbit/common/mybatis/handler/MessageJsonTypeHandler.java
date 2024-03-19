package cn.bravedawn.rabbit.common.mybatis.handler;

import cn.bravedawn.rabbit.api.Message;
import cn.bravedawn.rabbit.common.util.FastJsonConvertUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : depers
 * @program : rabbit-parent
 * @description: 类型处理器
 * @date : Created in 2021/3/15 21:42
 */
public class MessageJsonTypeHandler extends BaseTypeHandler<Message> {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Message message, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, FastJsonConvertUtil.convertObjectToJSON(message));
    }

    @Override
    public Message getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        String value = resultSet.getString(columnName);
        if (StringUtils.isNotBlank(value)) {
            return FastJsonConvertUtil.convertJSONToObject(value, Message.class);
        }
        return null;
    }

    @Override
    public Message getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
        String value = resultSet.getString(columnIndex);
        if (StringUtils.isNotBlank(value)) {
            return FastJsonConvertUtil.convertJSONToObject(value, Message.class);
        }
        return null;
    }

    @Override
    public Message getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
        String value = callableStatement.getString(columnIndex);
        if (StringUtils.isNotBlank(value)) {
            return FastJsonConvertUtil.convertJSONToObject(value, Message.class);
        }

        return null;
    }
}
