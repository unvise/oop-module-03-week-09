package com.unvise.oop.entity;

import com.unvise.oop.orm.annotation.Column;
import com.unvise.oop.orm.annotation.ManyToOne;
import com.unvise.oop.orm.annotation.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ticket")
public class Ticket implements Identifiable<Long> {
    @Column(primaryKey = true)
    private Long id;
    @Column(name = "price")
    private Double price;
    @Column
    private String currency;
    @ToString.Exclude
    @ManyToOne
    @Column(name = "board_id")
    private Board board;
}
