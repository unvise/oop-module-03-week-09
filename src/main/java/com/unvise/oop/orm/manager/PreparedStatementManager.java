package com.unvise.oop.orm.manager;

import com.unvise.oop.entity.Identifiable;
import com.unvise.oop.orm.ResultSetMapper;
import com.unvise.oop.orm.utils.ThrowingFunction;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface PreparedStatementManager {
    void executeUpdate(String sql) throws SQLException;

    void executeUpdate(String sql, List<Object> values) throws SQLException;

    <T> List<T> executeQuery(String sql, Function<ResultSet, List<T>> resultSetListFunction);

    void fillPreparedStatement(PreparedStatement preparedStatement, List<Object> values);

    static <T extends Identifiable<?>> List<T> findAll(PreparedStatementManager preparedStatementManager, ResultSetMapper<T> resultSetMapper, String sql) {
        return preparedStatementManager.executeQuery(sql, ThrowingFunction.wrap(resultSet -> {
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                T map = resultSetMapper.map(preparedStatementManager, resultSet);
                result.add(map);
            }
            return result;
        }));
    }
}
