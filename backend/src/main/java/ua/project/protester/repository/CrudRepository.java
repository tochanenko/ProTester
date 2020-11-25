package ua.project.protester.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

public interface CrudRepository<T> {
    int save(T entity);

    default void saveAll(Iterable<T> entities) {
        entities.forEach(this::save);
    }

    Optional<T> findById(Long id);

    List<T> findAll();

    void update(T entity);

    default void updateAll(Iterable<T> entities) {
        entities.forEach(this::update);
    }

    void delete(T entity);

    default void deleteAll(Iterable<T> entities) {
        entities.forEach(this::delete);
    }

    default void deleteAll() {
        this.deleteAll(this.findAll());
    }

    default Long count() {
        return StreamSupport.stream(findAll().spliterator(), false).count();
    }

    default boolean exists(Long id) {
        return findById(id).isPresent();
    }
}
