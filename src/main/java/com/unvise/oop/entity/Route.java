package com.unvise.oop.entity;

import com.unvise.oop.orm.annotation.Column;
import com.unvise.oop.orm.annotation.OneToMany;
import com.unvise.oop.orm.annotation.Table;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "route")
public class Route implements Identifiable<Long> {
    @Column(primaryKey = true)
    private Long id;
    @Column
    private String name;
    @Singular
    @OneToMany
    private List<RoutePath> routePaths;
}