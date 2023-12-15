package io.github.basicfrag.persistence.dao;




import java.util.List;
import java.util.Optional;



public interface IDao<T> {

    void persistEntity(T entity);

    Optional<List<T>> findAllEntities();

    Optional<T> findEntityById (Long id);

     void updateEntity(T entity);

    void removeEntity(Long id);
}
