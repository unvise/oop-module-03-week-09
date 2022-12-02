package com.unvise.oop.orm.meta;

import com.unvise.oop.orm.annotation.Column;
import com.unvise.oop.orm.annotation.ManyToOne;
import com.unvise.oop.orm.annotation.OneToMany;
import com.unvise.oop.orm.annotation.OneToOne;
import lombok.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.Objects.nonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeanFieldInfo {
    private Field field;
    private Method getter;
    private Method setter;
    @ToString.Exclude
    private TableInfo tableInfo;

    public Optional<Map.Entry<String, BeanFieldInfo>> getPrimaryKeyColumn() {
        return getColumn(this::isColumnHasPK);
    }

    public Optional<Map.Entry<String, BeanFieldInfo>> getOneToOneColumn() {
        return getColumn(this::isOneToOne);
    }

    public Optional<Map.Entry<String, BeanFieldInfo>> getManyToOneColumn() {
        return getColumn(this::isManyToOne);
    }

    public Optional<Map.Entry<String, BeanFieldInfo>> getColumn() {
        return getColumn(() -> true);
    }

    public String getColumnName() {
        Column column = field.getAnnotation(Column.class);
        return isColumn() && column.name().equals("")
                ? field.getName()
                : column.name();
    }

    public Boolean isColumnHasPK() {
        return isColumn() && field.getAnnotation(Column.class).primaryKey();
    }

    public Boolean isOneToOne() {
        return nonNull(field.getAnnotation(OneToOne.class));
    }

    public Boolean isOneToMany() {
        return nonNull(field.getAnnotation(OneToMany.class));
    }

    public Boolean isManyToOne() {
        return nonNull(field.getAnnotation(ManyToOne.class));
    }

    public Boolean isColumn() {
        return nonNull(field.getAnnotation(Column.class));
    }

    private Optional<Map.Entry<String, BeanFieldInfo>> getColumn(Supplier<Boolean> booleanSupplier) {
        return isColumn() && booleanSupplier.get()
                ? Optional.of(Map.entry(getColumnName(), this))
                : Optional.empty();
    }
}
