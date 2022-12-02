package com.unvise.oop.orm.manager.strategy;

import com.unvise.oop.entity.Identifiable;
import com.unvise.oop.orm.generator.SQLQueryGenerator;
import com.unvise.oop.orm.manager.ORMManager;
import com.unvise.oop.orm.manager.PreparedStatementManager;
import com.unvise.oop.orm.meta.BeanFieldInfo;
import com.unvise.oop.orm.meta.TableInfo;
import lombok.SneakyThrows;

import java.lang.reflect.ParameterizedType;
import java.sql.ResultSet;
import java.util.List;

public class OneToManySetterStrategy implements SetterStrategy {
    @SneakyThrows
    @Override
    public <T extends Identifiable<?>> void invoke(BeanFieldInfo beanFieldInfo,
                                                   T instance,
                                                   ResultSet resultSet,
                                                   PreparedStatementManager preparedStatementManager) {
        Class<T> type = (Class<T>) ((ParameterizedType) beanFieldInfo.getField().getGenericType()).getActualTypeArguments()[0];
        TableInfo internalEntityTableInfo = ORMManager.getTableInfo(type);
        String fieldName = beanFieldInfo.getTableInfo().getTableName().concat("_id");
        String selectByIdQuery = SQLQueryGenerator.selectByFieldWhere(
                internalEntityTableInfo,
                fieldName,
                List.of(instance.getId().toString())
        );
        List<T> instances = preparedStatementManager.executeQuery(
                selectByIdQuery,
                SetterStrategy.findAllFunction(preparedStatementManager, type)
        );
        setManyToOne(internalEntityTableInfo, instances, instance);
        beanFieldInfo.getSetter().invoke(instance, instances);
    }

    @SneakyThrows
    private <T> void setManyToOne(TableInfo tableInfo, List<T> instances, Object oneToManyInstance) {
        for (var bean : tableInfo.getAllBeanFieldInfo())
            for (var instance : instances)
                if (bean.isManyToOne() && bean.getTableInfo().getTableClass().equals(bean.getField().getType()))
                    bean.getSetter().invoke(oneToManyInstance, instance);
    }
}
