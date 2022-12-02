package com.unvise.oop.orm.manager.strategy;

import com.unvise.oop.entity.Identifiable;
import com.unvise.oop.orm.manager.PreparedStatementManager;
import com.unvise.oop.orm.meta.BeanFieldInfo;
import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CommonSetterStrategy implements SetterStrategy {
    @SneakyThrows
    @Override
    public <T extends Identifiable<?>> void invoke(BeanFieldInfo beanFieldInfo,
                                                T instance,
                                                ResultSet resultSet,
                                                PreparedStatementManager preparedStatementManager) {
        String columnName = beanFieldInfo.getColumnName();
        if (resultSet.getObject(columnName) instanceof Timestamp timestamp) {
            if (beanFieldInfo.getField().getType().equals(LocalDateTime.class)) {
                beanFieldInfo.getSetter().invoke(instance, timestamp.toLocalDateTime());
                return;
            }
            if (beanFieldInfo.getField().getType().equals(LocalDate.class)) {
                beanFieldInfo.getSetter().invoke(instance, timestamp.toLocalDateTime().toLocalDate());
                return;
            }
            beanFieldInfo.getSetter().invoke(instance, timestamp);
            return;
        }
        beanFieldInfo.getSetter().invoke(instance, resultSet.getObject(columnName));
    }
}
