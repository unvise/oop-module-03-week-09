package com.unvise.oop.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    List<T> findAll();

    Optional<T> findById(ID id);

    T save(T instance);

    T update(T instance);

    T delete(ID id);
}
