package com.unvise.oop.repository.impl;

import com.unvise.oop.entity.Route;
import com.unvise.oop.repository.AbstractCrudRepository;

import javax.sql.DataSource;

public class RouteRepository extends AbstractCrudRepository<Route, Long> {
    public RouteRepository(DataSource dataSource) {
        super(Route.class, dataSource);
    }
}
