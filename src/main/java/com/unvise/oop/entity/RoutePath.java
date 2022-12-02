package com.unvise.oop.entity;

import com.unvise.oop.orm.annotation.Column;
import com.unvise.oop.orm.annotation.ManyToOne;
import com.unvise.oop.orm.annotation.OneToOne;
import com.unvise.oop.orm.annotation.Table;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "route_path")
public class RoutePath implements Identifiable<Long> {
    @Column(primaryKey = true)
    private Long id;
    @Column(name = "from_id")
    @OneToOne
    private Geolocation from;
    @Column(name = "to_id")
    @OneToOne
    private Geolocation to;
    @Column
    private Double progress;
    @ToString.Exclude
    @ManyToOne
    @Column(name = "route_id")
    private Route route;
}
