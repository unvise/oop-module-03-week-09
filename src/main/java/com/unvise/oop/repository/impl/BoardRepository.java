package com.unvise.oop.repository.impl;

import com.unvise.oop.entity.Board;
import com.unvise.oop.repository.AbstractCrudRepository;

import javax.sql.DataSource;

public class BoardRepository extends AbstractCrudRepository<Board, Long> {
    public BoardRepository(DataSource dataSource) {
        super(Board.class, dataSource);
    }
}
