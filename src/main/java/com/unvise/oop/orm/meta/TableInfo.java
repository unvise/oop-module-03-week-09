package com.unvise.oop.orm.meta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.Objects.isNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TableInfo {
    private String tableName;
    private Class<?> tableClass;
    private Map.Entry<String, BeanFieldInfo> idColumn;
    @Builder.Default
    private Map<String, BeanFieldInfo> columns = new TreeMap<>();
    @Builder.Default
    private Map<String, BeanFieldInfo> oneToOne = new TreeMap<>();
    @Builder.Default
    private Map<String, BeanFieldInfo> oneToMany = new TreeMap<>();
    @Builder.Default
    private Map<String, BeanFieldInfo> manyToOne = new TreeMap<>();

    public List<BeanFieldInfo> getAllBeanFieldInfo() {
        List<BeanFieldInfo> beanFieldInfos = new ArrayList<>();
        beanFieldInfos.addAll(columns.values().stream().toList());
        beanFieldInfos.addAll(oneToMany.values().stream().toList());
        return beanFieldInfos;
    }

    public Boolean isTableHasReferences() {
        return !getOneToOne().isEmpty()
                || !getManyToOne().isEmpty()
                || !getOneToMany().isEmpty();
    }

    public void fillColumns(List<BeanFieldInfo> beanFieldInfos) {
        for (BeanFieldInfo beanFieldInfo : beanFieldInfos) {
            if (!isNull(beanFieldInfo)) {
                beanFieldInfo.getPrimaryKeyColumn().ifPresent(c -> setIdColumn(Map.entry(c.getKey(), c.getValue())));
                beanFieldInfo.getColumn().ifPresent(c -> getColumns().put(c.getKey(), c.getValue()));
                beanFieldInfo.getOneToOneColumn().ifPresent(c -> getOneToOne().put(c.getKey(), c.getValue()));
                beanFieldInfo.getManyToOneColumn().ifPresent(c -> getManyToOne().put(c.getKey(), c.getValue()));
                if (beanFieldInfo.isOneToMany())
                    getOneToMany().put(beanFieldInfo.getField().getName(), beanFieldInfo);
            }
        }
    }
}
