package com.unvise.oop;

import com.unvise.oop.config.DatabaseConfig;
import com.unvise.oop.repository.impl.*;
import com.unvise.oop.orm.database.DatabaseInitializer;

import javax.sql.DataSource;

public class ORMApplication {
    public static void main(String[] args) {
        DatabaseConfig databaseConfig = new DatabaseConfig();
        DataSource h2FileDatabase = databaseConfig.createH2FileDatabase("./database/test");
        DatabaseInitializer.init(h2FileDatabase);
        BoardRepository boardRepository = new BoardRepository(h2FileDatabase);
        System.out.println(boardRepository.findAll());
    }
}
