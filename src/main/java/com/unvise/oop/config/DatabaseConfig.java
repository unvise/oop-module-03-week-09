package com.unvise.oop.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConfig {
    public DataSource createH2FileDatabase(String path) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:file:".concat(path));
        config.setUsername("");
        config.setPassword("");
        return new HikariDataSource(config);
    }

    public DataSource createH2MemDatabase() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.h2.Driver");
        config.setJdbcUrl("jdbc:h2:mem:test");
        config.setUsername("");
        config.setPassword("");
        return new HikariDataSource(config);
    }
}