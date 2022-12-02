package com.unvise.oop.repository.impl;

import com.unvise.oop.entity.Geolocation;
import com.unvise.oop.repository.AbstractCrudRepository;

import javax.sql.DataSource;

public class GeolocationRepository extends AbstractCrudRepository<Geolocation, Long> {
    public GeolocationRepository(DataSource dataSource) {
        super(Geolocation.class, dataSource);
    }
}
