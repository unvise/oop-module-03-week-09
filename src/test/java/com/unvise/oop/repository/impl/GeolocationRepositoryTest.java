package com.unvise.oop.repository.impl;

import com.unvise.oop.config.DatabaseConfig;
import com.unvise.oop.data.SampleStorage;
import com.unvise.oop.entity.Geolocation;
import com.unvise.oop.orm.database.DatabaseInitializer;
import com.unvise.oop.repository.RepositoryUtils;
import org.junit.jupiter.api.*;

import javax.sql.DataSource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тест репозитория Geolocation")
class GeolocationRepositoryTest {
    private static final DataSource H2_MEM_DATABASE = new DatabaseConfig().createH2MemDatabase();
    private final GeolocationRepository geolocationRepository = new GeolocationRepository(H2_MEM_DATABASE);

    @BeforeAll
    static void init() {
        DatabaseInitializer.init(H2_MEM_DATABASE);
    }

    @AfterEach
    void clearGeolocations() {
        RepositoryUtils.deleteAll(geolocationRepository, geolocationRepository.findAll());
    }

    @DisplayName("Проверка на поиск всех сущностей")
    @Test
    void findAll() {
        // given
        List<Geolocation> geolocations = SampleStorage.GEOLOCATIONS;
        // when
        geolocations.forEach(geolocationRepository::save);
        // then
        assertEquals(geolocations, geolocationRepository.findAll());
    }

    @DisplayName("Проверка на поиск сущности по ее id")
    @Test
    void findById() {
        // given
        List<Geolocation> geolocations = SampleStorage.GEOLOCATIONS;
        // when
        geolocations.forEach(geolocationRepository::save);
        // then
        assertEquals(geolocations.get(1), geolocationRepository.findById(2L).orElseThrow(RuntimeException::new));
    }

    @DisplayName("Проверка на сохранение сущности")
    @Test
    @Order(1)
    void save() {
        // given
        Geolocation geolocation = SampleStorage.GEOLOCATIONS.get(0);
        // when
        geolocationRepository.save(geolocation);
        // then
        assertEquals(geolocation, geolocationRepository.findById(1L).orElseThrow(RuntimeException::new));
    }

    @DisplayName("Проверка на обновление сущности")
    @Test
    void update() {
        // given
        Geolocation geolocation = SampleStorage.GEOLOCATIONS.get(0);
        // when
        geolocationRepository.save(geolocation);
        geolocation.setLatitude(0d);
        geolocationRepository.update(geolocation);
        // then
        assertEquals(geolocation, geolocationRepository.findById(1L).orElseThrow(RuntimeException::new));
    }

    @DisplayName("Проверка на удаление сущности")
    @Test
    void delete() {
        // given
        Geolocation geolocation = SampleStorage.GEOLOCATIONS.get(0);
        // when
        geolocationRepository.save(geolocation);
        // then
        assertFalse(geolocationRepository.findAll().isEmpty());
        geolocationRepository.delete(geolocation.getId());
        assertTrue(geolocationRepository.findAll().isEmpty());

    }
}