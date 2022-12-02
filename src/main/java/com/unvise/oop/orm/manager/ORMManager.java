package com.unvise.oop.orm.manager;

import com.unvise.oop.orm.annotation.Table;
import com.unvise.oop.exception.ORMException;
import com.unvise.oop.orm.meta.BeanFieldInfo;
import com.unvise.oop.orm.meta.TableInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

@Slf4j
public class ORMManager {
    public static List<Class<?>> findAllTableEntities() {
        Reflections reflections = new Reflections("com.unvise.oop");
        return reflections.getTypesAnnotatedWith(Table.class).stream().toList();
    }

    public static TableInfo getTableInfo(Class<?> clazz) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(clazz.getAnnotation(Table.class).name());
        tableInfo.setTableClass(clazz);
        tableInfo.fillColumns(getBeanFieldInfoInDescending(tableInfo, clazz));
        if (isNull(tableInfo.getIdColumn())) {
            throw new ORMException("Первичный ключ не определен");
        }
        return tableInfo;
    }

    @SneakyThrows
    public static <T> T getFieldValue(BeanFieldInfo beanFieldInfo, T object) {
        return (T) beanFieldInfo.getGetter().invoke(object);
    }

    public static <T> List<T> getFieldsValuesWithoutId(TableInfo tableInfo, T instance) {
        return getBeanFieldInfoInDescending(tableInfo, instance.getClass()).stream()
                .filter(beanFieldInfo -> !beanFieldInfo.isColumnHasPK())
                .map(beanFieldInfo -> getFieldValue(beanFieldInfo, instance))
                .toList();
    }

    public static <T> List<T> getFieldValues(TableInfo tableInfo, T instance) {
        return tableInfo.getColumns().values().stream()
                .map(beanFieldInfo -> getFieldValue(beanFieldInfo, instance))
                .toList();
    }

    @SneakyThrows
    public static List<BeanFieldInfo> getBeanFieldInfoInDescending(TableInfo tableInfo, Class<?> clazz) {
        return Arrays.stream(Introspector.getBeanInfo(clazz).getPropertyDescriptors())
                .filter(propertyDescriptor -> !propertyDescriptor.getPropertyType().equals(Class.class))
                .map(propertyDescriptor -> createBeanFieldInfo(tableInfo, propertyDescriptor, clazz))
                .toList();
    }

    @SneakyThrows
    private static BeanFieldInfo createBeanFieldInfo(TableInfo tableInfo, PropertyDescriptor propertyDescriptor, Class<?> clazz) {
        Field field = clazz.getDeclaredField(propertyDescriptor.getName());
        return BeanFieldInfo.builder()
                .field(field)
                .getter(propertyDescriptor.getReadMethod())
                .setter(propertyDescriptor.getWriteMethod())
                .tableInfo(tableInfo)
                .build();
    }
}
