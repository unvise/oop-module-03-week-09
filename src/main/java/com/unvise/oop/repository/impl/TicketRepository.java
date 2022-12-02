package com.unvise.oop.repository.impl;

import com.unvise.oop.entity.Ticket;
import com.unvise.oop.repository.AbstractCrudRepository;

import javax.sql.DataSource;

public class TicketRepository extends AbstractCrudRepository<Ticket, Long> {
    public TicketRepository(DataSource dataSource) {
        super(Ticket.class, dataSource);
    }
}
