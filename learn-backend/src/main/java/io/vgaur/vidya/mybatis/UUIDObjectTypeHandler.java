package io.vgaur.vidya.mybatis;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

/**
 * Add this type handler to your MyBatis SqlSessionFactory's configuration like so:
 *
 * sessionFactory.getConfiguration()
 *               .getTypeHandlerRegistry()
 *               .register(UUID.class, UUIDObjectTypeHandler);
 *
 * Unlike UUIDTypeHandler this expects the JDBC driver to have native support for the UUID type. For example,
 * PostgreSQL has a "uuid" database column.
 */
@MappedTypes(UUID.class)
public class UUIDObjectTypeHandler extends BaseTypeHandler<UUID> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setObject(i, parameter, Types.OTHER);
    }

    @Override
    public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return (UUID) rs.getObject(columnName);
    }

    @Override
    public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return (UUID) rs.getObject(columnIndex);
    }

    @Override
    public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (UUID) cs.getObject(columnIndex);
    }
}
