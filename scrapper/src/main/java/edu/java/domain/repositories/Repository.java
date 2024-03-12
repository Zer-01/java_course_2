package edu.java.domain.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    void add(T entity);

    void remove(long id);

    List<T> findAll();

    Optional<T> findById(long id);
}
