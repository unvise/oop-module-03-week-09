package com.unvise.oop.entity;

import com.unvise.oop.orm.annotation.Column;
import com.unvise.oop.orm.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "geolocation")
public class Geolocation implements Identifiable<Long> {
    @Column(primaryKey = true)
    private Long id;
    @Column
    private LocalDateTime dateTime;
    @Column
    private Double latitude;
    @Column
    private Double longitude;
}
