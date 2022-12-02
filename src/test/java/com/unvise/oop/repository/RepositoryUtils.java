package com.unvise.oop.repository;

import com.unvise.oop.entity.Identifiable;

import java.io.Serializable;
import java.util.List;

public class RepositoryUtils {
    public static <T extends Identifiable<ID>, ID extends Serializable> void deleteAll(AbstractCrudRepository<T, ID> repository, List<T> instances) {
        instances.stream().map(Identifiable::getId).forEach(repository::delete);
    }
}
