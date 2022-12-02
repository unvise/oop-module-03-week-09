package com.unvise.oop.orm;

import com.unvise.oop.entity.Identifiable;
import com.unvise.oop.orm.manager.ORMManager;
import com.unvise.oop.orm.manager.PreparedStatementManager;
import com.unvise.oop.orm.manager.resolver.SetterStrategyResolver;
import com.unvise.oop.orm.manager.strategy.SetterStrategy;
import com.unvise.oop.orm.meta.TableInfo;
import com.unvise.oop.orm.utils.ProxyUtils;

import java.sql.ResultSet;

public class ResultSetMapper<T extends Identifiable<?>> {
    private final Class<T> clazz;
    public ResultSetMapper(Class<T> clazz) {
        this.clazz = clazz;
    }

    public T map(PreparedStatementManager preparedStatementManager, ResultSet resultSet) {
        T instance = ProxyUtils.cglibProxy(clazz);
        TableInfo tableInfo = ORMManager.getTableInfo(instance.getClass().getSuperclass());
        for (var beanInfo : tableInfo.getAllBeanFieldInfo()) {
            SetterStrategy setterStrategy = SetterStrategyResolver.resolve(beanInfo);
            setterStrategy.invoke(beanInfo, instance, resultSet, preparedStatementManager);
        }
        return instance;
    }
}
