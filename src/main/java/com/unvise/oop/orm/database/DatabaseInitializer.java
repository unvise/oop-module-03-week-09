package com.unvise.oop.orm.database;

import com.unvise.oop.orm.generator.SQLQueryGenerator;
import com.unvise.oop.orm.manager.ORMManager;
import com.unvise.oop.orm.manager.PreparedStatementManager;
import com.unvise.oop.orm.manager.PreparedStatementManagerImpl;
import com.unvise.oop.orm.meta.TableInfo;
import lombok.SneakyThrows;

import javax.sql.DataSource;

public class DatabaseInitializer {
    public static void init(DataSource dataSource) {
        PreparedStatementManager preparedStatementManager = new PreparedStatementManagerImpl(dataSource);
        createTables(preparedStatementManager);
        addColumns(preparedStatementManager);
        addPKConstraints(preparedStatementManager);
        addFKConstraints(preparedStatementManager);
    }

    @SneakyThrows
    private static void createTables(PreparedStatementManager preparedStatementManager) {
        for (var clazz : ORMManager.findAllTableEntities()) {
            TableInfo tableInfo = ORMManager.getTableInfo(clazz);
            String tableQuery = SQLQueryGenerator.createTable(tableInfo);
            preparedStatementManager.executeUpdate(tableQuery);
        }
    }

    @SneakyThrows
    private static void addColumns(PreparedStatementManager preparedStatementManager) {
        for (var clazz : ORMManager.findAllTableEntities()) {
            TableInfo tableInfo = ORMManager.getTableInfo(clazz);
            if (tableInfo.isTableHasReferences()) {
                String addClassesAsColumnsQuery = SQLQueryGenerator.addClassesAsColumns(tableInfo);
                preparedStatementManager.executeUpdate(addClassesAsColumnsQuery);
            }
        }
    }

    @SneakyThrows
    private static void addPKConstraints(PreparedStatementManager preparedStatementManager) {
        for (var clazz : ORMManager.findAllTableEntities()) {
            TableInfo tableInfo = ORMManager.getTableInfo(clazz);
            String pkConstraints = SQLQueryGenerator.addPKConstraints(tableInfo);
            preparedStatementManager.executeUpdate(pkConstraints);
        }
    }

    @SneakyThrows
    private static void addFKConstraints(PreparedStatementManager preparedStatementManager) {
        for (var clazz : ORMManager.findAllTableEntities()) {
            TableInfo tableInfo = ORMManager.getTableInfo(clazz);
            if (tableInfo.isTableHasReferences()) {
                String oneToOneFieldsConstraints = SQLQueryGenerator.addOneToOneFields(tableInfo);
                String manyToOneFieldsConstraints = SQLQueryGenerator.addManyToOneFields(tableInfo);
                if (!oneToOneFieldsConstraints.isEmpty())
                    preparedStatementManager.executeUpdate(oneToOneFieldsConstraints);
                if (!manyToOneFieldsConstraints.isEmpty())
                    preparedStatementManager.executeUpdate(manyToOneFieldsConstraints);
            }
        }
    }
}
