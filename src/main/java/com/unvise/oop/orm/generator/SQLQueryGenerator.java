package com.unvise.oop.orm.generator;

import com.unvise.oop.orm.annotation.Table;
import com.unvise.oop.orm.meta.BeanFieldInfo;
import com.unvise.oop.orm.meta.TableInfo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public class SQLQueryGenerator {
    public static String createTable(TableInfo tableInfo) {
        List<String> values = tableInfo.getColumns().keySet().stream()
                .map(k -> {
                    Field field = tableInfo.getColumns().get(k).getField();
                    String sqlType = ToSQLTypesResolver.resolve(field.getType());
                    return sqlType.isEmpty()
                            ? ""
                            : k.concat(" ")
                            .concat(sqlType)
                            .concat(" NOT NULL");
                })
                .filter(s -> !s.isEmpty())
                .toList();
        return String.format(
                "CREATE TABLE IF NOT EXISTS %s (%s);",
                tableInfo.getTableName(),
                joinColumns(values)
        );
    }

    public static String addClassesAsColumns(TableInfo tableInfo) {
        List<String> values = tableInfo.getAllBeanFieldInfo().stream()
                .filter(beanFieldInfo -> beanFieldInfo.isOneToOne() || beanFieldInfo.isManyToOne())
                .map(beanFieldInfo -> beanFieldInfo.getColumnName().concat(" INTEGER"))
                .toList();
        return values.stream()
                .map(value -> addClassAsColumn(tableInfo, value))
                .collect(Collectors.joining());
    }

    public static String addClassAsColumn(TableInfo tableInfo, String value) {
        return String.format(
                "ALTER TABLE %s ADD COLUMN IF NOT EXISTS %s;",
                tableInfo.getTableName(),
                value
        );
    }

    public static String addOneToOneFields(TableInfo tableInfo) {
        List<BeanFieldInfo> values = tableInfo.getOneToOne().values().stream()
                .filter(BeanFieldInfo::isOneToOne)
                .toList();
        return values.stream()
                .map(SQLQueryGenerator::addFKConstraint)
                .collect(Collectors.joining("\n"));
    }

    public static String addManyToOneFields(TableInfo tableInfo) {
        List<BeanFieldInfo> values = tableInfo.getManyToOne().values().stream()
                .filter(BeanFieldInfo::isManyToOne)
                .toList();
        return values.stream()
                .map(SQLQueryGenerator::addFKConstraint)
                .collect(Collectors.joining("\n"));
    }

    public static String addPKConstraints(TableInfo tableInfo) {
        String columnName = tableInfo.getIdColumn().getKey();
        String constraints = String.format(
                "CONSTRAINT IF NOT EXISTS %s PRIMARY KEY (%s)",
                "pk_".concat(tableInfo.getTableName()).concat("_").concat(columnName),
                columnName
        );
        return String.format(
                "ALTER TABLE %s ADD %s ",
                tableInfo.getTableName(),
                constraints
        );
    }

    public static String addFKConstraint(BeanFieldInfo beanFieldInfo) {
        String columnName = beanFieldInfo.getColumnName();
        String constraints = String.format(
                "CONSTRAINT IF NOT EXISTS %s FOREIGN KEY (%s) REFERENCES %s(%s);",
                "fk_".concat(beanFieldInfo.getTableInfo().getTableName()).concat("_").concat(columnName),
                columnName,
                beanFieldInfo.getField().getType().getAnnotation(Table.class).name(),
                beanFieldInfo.getTableInfo().getIdColumn().getKey()
        );
        return String.format(
                "ALTER TABLE %s ADD %s ",
                beanFieldInfo.getTableInfo().getTableName(),
                constraints
        );
    }

    public static String insert(TableInfo tableInfo) {
        List<String> columnNames = tableInfo.getColumns().keySet().stream().toList();
        return String.format(
                "INSERT INTO %s (%s) VALUES (%s);",
                tableInfo.getTableName(),
                joinColumns(columnNames),
                queries(columnNames.size())
        );
    }

    public static String select(String tableName, List<String> valueNames) {
        String id = "*";
        if (!valueNames.isEmpty()) {
            id = String.join(", ", valueNames);
        }
        return String.format("SELECT %s FROM %s;", id, tableName);
    }

    public static String selectById(TableInfo tableInfo, String id) {
        String pk = tableInfo.getIdColumn().getKey();
        return String.format(
                "SELECT * FROM %s WHERE %s;",
                tableInfo.getTableName(),
                String.format("%s = %s", pk, id)
        );
    }

    public static String selectByFieldWhere(TableInfo tableInfo, String fieldName, List<String> values) {
        return String.format(
                "SELECT * FROM %s WHERE %s IN (%s);",
                tableInfo.getTableName(),
                fieldName,
                joinColumns(values)
        );
    }

    public static String updateById(TableInfo tableInfo, String id) {
        List<String> values = tableInfo.getColumns().values().stream()
                .filter(v -> !v.isColumnHasPK())
                .map(v -> v.getColumnName().concat(" = ?"))
                .toList();
        String pk = tableInfo.getIdColumn().getKey();
        return String.format(
                "UPDATE %s SET %s WHERE %s;",
                tableInfo.getTableName(),
                joinColumns(values),
                String.format("%s = %s", pk, id)
        );
    }

    public static String deleteById(TableInfo tableInfo, String id) {
        String pk = tableInfo.getIdColumn().getKey();
        return String.format(
                "DELETE FROM %s WHERE %s;",
                tableInfo.getTableName(),
                String.format("%s = %s", pk, id)
        );
    }

    private static String queries(Integer size) {
        return "?, ".repeat(Math.max(0, size - 1)).concat("?");
    }

    private static String joinColumns(List<String> valueNames) {
        return String.join(", ", valueNames);
    }
}
