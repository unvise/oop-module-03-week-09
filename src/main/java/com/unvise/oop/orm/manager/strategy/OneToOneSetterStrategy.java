package com.unvise.oop.orm.manager.strategy;

import com.unvise.oop.entity.Identifiable;
import com.unvise.oop.orm.generator.SQLQueryGenerator;
import com.unvise.oop.orm.manager.ORMManager;
import com.unvise.oop.orm.manager.PreparedStatementManager;
import com.unvise.oop.orm.meta.BeanFieldInfo;
import com.unvise.oop.orm.meta.TableInfo;
import com.unvise.oop.orm.utils.ProxyUtils;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.List;

public class OneToOneSetterStrategy implements SetterStrategy {
    @SneakyThrows
    @Override
    public <T extends Identifiable<?>> void invoke(BeanFieldInfo beanFieldInfo,
                                                   T instance,
                                                   ResultSet resultSet,
                                                   PreparedStatementManager preparedStatementManager) {
        String columnName = beanFieldInfo.getColumnName();
        Class<T> type = (Class<T>) beanFieldInfo.getField().getType();
        TableInfo internalEntityTableInfo = ORMManager.getTableInfo(type);
        T proxy = ProxyUtils.cglibProxy(type);
        beanFieldInfo.getSetter().invoke(instance, proxy);
        String selectByIdQuery = SQLQueryGenerator.selectById(internalEntityTableInfo, resultSet.getString(columnName));
        List<T> ts = preparedStatementManager.executeQuery(
                selectByIdQuery,
                SetterStrategy.findAllFunction(preparedStatementManager, type)
        );
        beanFieldInfo.getSetter().invoke(instance, ts.get(0));
    }
}
