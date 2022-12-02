package com.unvise.oop.entity;

import com.unvise.oop.orm.annotation.Column;
import com.unvise.oop.orm.annotation.OneToMany;
import com.unvise.oop.orm.annotation.OneToOne;
import com.unvise.oop.orm.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "board")
public class Board implements Identifiable<Long> {
    @Column(primaryKey = true)
    private Long id;
    @Column
    private String name;
    @Column(name = "start_date_time")
    private LocalDateTime startDateTime;
    @Column(name = "end_date_time")
    private LocalDateTime endDateTime;
    @OneToOne
    @Column(name = "route_id")
    private Route route;
    @OneToMany
    @Builder.Default
    private List<Ticket> tickets = new ArrayList<>();
}
