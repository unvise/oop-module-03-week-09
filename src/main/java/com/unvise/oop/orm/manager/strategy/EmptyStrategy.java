package com.unvise.oop.orm.manager.strategy;

import com.unvise.oop.entity.Identifiable;
import com.unvise.oop.orm.manager.PreparedStatementManager;
import com.unvise.oop.orm.meta.BeanFieldInfo;

import java.sql.ResultSet;

public class EmptyStrategy implements SetterStrategy{
    @Override
    public <T extends Identifiable<?>> void invoke(BeanFieldInfo beanFieldInfo,
                                                   T instance,
                                                   ResultSet resultSet,
                                                   PreparedStatementManager preparedStatementManager) {
    }
}
