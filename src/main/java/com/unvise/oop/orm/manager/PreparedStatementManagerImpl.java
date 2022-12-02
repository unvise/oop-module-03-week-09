package com.unvise.oop.orm.manager;

import com.unvise.oop.entity.Identifiable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class PreparedStatementManagerImpl implements PreparedStatementManager {
    private final DataSource dataSource;

    public PreparedStatementManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void executeUpdate(String sql) throws SQLException {
        executeUpdate(sql, Collections.emptyList());
    }

    @Override
    public void executeUpdate(String sql, List<Object> values) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            fillPreparedStatement(preparedStatement, values);
            log.info("Выполнение SQL-запроса - {}", sql);
            preparedStatement.executeUpdate();
        }
    }

    @SneakyThrows
    @Override
    public <T> List<T> executeQuery(String sql, Function<ResultSet, List<T>> resultSetListFunction) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            fillPreparedStatement(preparedStatement, Collections.emptyList());
            log.info("Выполнение SQL-запроса - {}", sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetListFunction.apply(resultSet);
        }
    }

    @SneakyThrows
    @Override
    public void fillPreparedStatement(PreparedStatement preparedStatement, List<Object> values) {
        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);
            if (value instanceof Date date) {
                Timestamp sqlDate = new Timestamp(date.getTime());
                preparedStatement.setTimestamp(i + 1, sqlDate);
                continue;
            }
            if (value instanceof Identifiable<?> identifiable) {
                preparedStatement.setObject(i + 1, identifiable.getId());
                continue;
            }
            if (value instanceof Iterable<?>) {
                continue;
            }
            preparedStatement.setObject(i + 1, value);
        }
    }
}
