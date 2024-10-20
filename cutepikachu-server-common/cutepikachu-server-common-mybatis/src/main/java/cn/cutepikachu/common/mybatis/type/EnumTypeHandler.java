package cn.cutepikachu.common.mybatis.type;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author <a href="https://github.com/cutepikachu-cn">笨蛋皮卡丘</a>
 * @version 1.0
 * @since 2024-10-20 12:29-27
 */
@MappedJdbcTypes(JdbcType.TINYINT)
@MappedTypes(Enum.class)
public class EnumTypeHandler<E extends Enum<E>> implements TypeHandler<E> {

    private final Class<E> type;

    public EnumTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.ordinal());
    }

    @Override
    public E getResult(ResultSet rs, String columnName) throws SQLException {
        int ordinal = rs.getInt(columnName);
        return getEnumByOrdinal(ordinal);
    }

    @Override
    public E getResult(ResultSet rs, int columnIndex) throws SQLException {
        int ordinal = rs.getInt(columnIndex);
        return getEnumByOrdinal(ordinal);
    }

    @Override
    public E getResult(CallableStatement cs, int columnIndex) throws SQLException {
        int ordinal = cs.getInt(columnIndex);
        return getEnumByOrdinal(ordinal);
    }

    private E getEnumByOrdinal(int ordinal) {
        E[] enumConstants = type.getEnumConstants();
        if (ordinal < 0 || ordinal >= enumConstants.length) {
            throw new IllegalArgumentException("Invalid ordinal for enum type");
        }
        return enumConstants[ordinal];
    }

}
