package com.unvise.oop.orm.manager.strategy;

import com.unvise.oop.entity.Identifiable;
import com.unvise.oop.orm.ResultSetMapper;
import com.unvise.oop.orm.manager.PreparedStatementManager;
import com.unvise.oop.orm.meta.BeanFieldInfo;
import com.unvise.oop.orm.utils.ThrowingFunction;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface SetterStrategy {
    <T extends Identifiable<?>> void invoke(BeanFieldInfo beanFieldInfo,
                                         T instance,
                                         ResultSet resultSet,
                                         PreparedStatementManager preparedStatementManager);

    static <T extends Identifiable<?>> Function<ResultSet, List<T>> findAllFunction(PreparedStatementManager preparedStatementManager, Class<T> clazz) {
        ResultSetMapper<T> resultSetMapper = new ResultSetMapper<>(clazz);
        return ThrowingFunction.wrap(resultSet -> {
            List<T> result = new ArrayList<>();
            while (resultSet.next()) {
                T map = resultSetMapper.map(preparedStatementManager, resultSet);
                result.add(map);
            }
            return result;
        });
    }
}
