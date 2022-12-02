package com.unvise.oop.data;

import com.unvise.oop.entity.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class SampleStorage {
    public static final List<Geolocation> GEOLOCATIONS = List.of(
            Geolocation.builder()
                    .id(1L)
                    .dateTime(LocalDateTime.of(2022, Month.FEBRUARY, 12, 22, 0))
                    .latitude(53.117750171752675)
                    .longitude(45.022817)
                    .build(),
            Geolocation.builder()
                    .id(2L)
                    .dateTime(LocalDateTime.of(2022, Month.FEBRUARY, 13, 3, 0))
                    .latitude(56.176298)
                    .longitude(92.481504)
                    .build()
    );

    public static final List<RoutePath> ROUTE_PATHS = List.of(
            RoutePath.builder()
                    .id(1L)
                    .from(GEOLOCATIONS.get(0))
                    .to(GEOLOCATIONS.get(1))
                    .progress(100D)
                    .route(Route.builder().id(1L).build())
                    .build()
    );

    public static final List<Route> ROUTES = List.of(
            Route.builder()
                    .id(1L)
                    .name("Маршрут «Рейс вечерний: Пенза - Красноярск»")
                    .routePaths(List.of(ROUTE_PATHS.get(0)))
                    .build()
    );

    public static final List<Board> BOARDS = List.of(
            Board.builder()
                    .id(1L)
                    .name("Рейс вечерний: Пенза - Красноярск")
                    .startDateTime(LocalDateTime.of(2022, Month.FEBRUARY, 12, 21, 30))
                    .endDateTime(LocalDateTime.of(2022, Month.FEBRUARY, 13, 3, 30))
                    .route(ROUTES.get(0))
                    .build()
    );

    public static final List<Ticket> TICKETS = List.of(
            Ticket.builder()
                    .id(1L)
                    .board(BOARDS.get(0))
                    .price(25000.0)
                    .currency("RUB")
                    .build()
    );
}
