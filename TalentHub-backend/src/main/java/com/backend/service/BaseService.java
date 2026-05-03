package com.backend.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, E, ID> {
    E create(T t);

    Optional<E> getById(ID id);

    List<E> getAll();

    Boolean deleteById(ID id);
}
