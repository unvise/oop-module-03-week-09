package com.unvise.oop.repository;

import com.unvise.oop.entity.Identifiable;
import com.unvise.oop.exception.EntityNotFoundException;
import com.unvise.oop.orm.ResultSetMapper;
import com.unvise.oop.orm.generator.SQLQueryGenerator;
import com.unvise.oop.orm.manager.ORMManager;
import com.unvise.oop.orm.manager.PreparedStatementManager;
import com.unvise.oop.orm.manager.PreparedStatementManagerImpl;
import com.unvise.oop.orm.meta.TableInfo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractCrudRepository<T extends Identifiable<ID>, ID extends Serializable>
        implements CrudRepository<T, ID> {
    private final PreparedStatementManagerImpl preparedStatementManager;
    private final ResultSetMapper<T> resultSetMapper;
    private final TableInfo tableInfo;

    protected AbstractCrudRepository(Class<T> clazz, DataSource dataSource) {
        this.tableInfo = ORMManager.getTableInfo(clazz);
        this.resultSetMapper = new ResultSetMapper<>(clazz);
        this.preparedStatementManager = new PreparedStatementManagerImpl(dataSource);
    }

    @Override
    public List<T> findAll() {
        String sql = SQLQueryGenerator.select(tableInfo.getTableName(), Collections.emptyList());
        return PreparedStatementManager.findAll(preparedStatementManager, resultSetMapper, sql);
    }

    @Override
    public Optional<T> findById(ID id) {
        String sql = SQLQueryGenerator.selectById(tableInfo, id.toString());
        List<T> all = PreparedStatementManager.findAll(preparedStatementManager, resultSetMapper, sql);
        return all.isEmpty() ? Optional.empty() : Optional.of(all.get(0));
    }

    @SneakyThrows
    @Override
    public T save(T instance) {
        List<Object> values = ORMManager.getFieldValues(tableInfo, instance);
        String sql = SQLQueryGenerator.insert(tableInfo);
        preparedStatementManager.executeUpdate(sql, values);
        return instance;
    }

    @SneakyThrows
    @Override
    public T update(T instance) {
        List<Object> values = ORMManager.getFieldsValuesWithoutId(tableInfo, instance);
        String findByIdQuery = SQLQueryGenerator.selectById(tableInfo, instance.getId().toString());
        List<T> all = PreparedStatementManager.findAll(preparedStatementManager, resultSetMapper, findByIdQuery);
        if (all.isEmpty()) {
            throw new EntityNotFoundException(String.format(
                    "Сущности(%s) с id(%s) не существует", tableInfo.getTableClass().getSimpleName(), instance.getId()
            ));
        }
        String updateByIdQuery = SQLQueryGenerator.updateById(tableInfo, instance.getId().toString());
        preparedStatementManager.executeUpdate(updateByIdQuery, values);
        return instance;
    }

    @SneakyThrows
    @Override
    public T delete(ID id) {
        String findByIdQuery = SQLQueryGenerator.selectById(tableInfo, id.toString());
        List<T> all = PreparedStatementManager.findAll(preparedStatementManager, resultSetMapper, findByIdQuery);
        if (all.isEmpty()) {
            throw new EntityNotFoundException(String.format(
                    "Сущности(%s) с id(%s) не существует", tableInfo.getTableClass().getSimpleName(), id
            ));
        }
        String deleteByIdQuery = SQLQueryGenerator.deleteById(tableInfo, id.toString());
        preparedStatementManager.executeUpdate(deleteByIdQuery);
        return all.get(0);
    }
}
