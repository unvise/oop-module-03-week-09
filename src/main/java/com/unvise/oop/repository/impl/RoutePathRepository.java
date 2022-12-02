package com.unvise.oop.repository.impl;

import com.unvise.oop.entity.RoutePath;
import com.unvise.oop.repository.AbstractCrudRepository;

import javax.sql.DataSource;

public class RoutePathRepository extends AbstractCrudRepository<RoutePath, Long> {
    public RoutePathRepository(DataSource dataSource) {
        super(RoutePath.class, dataSource);
    }
}
