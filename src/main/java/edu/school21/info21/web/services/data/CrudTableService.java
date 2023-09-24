package edu.school21.info21.web.services.data;

import java.util.List;

public interface CrudTableService<T, D> {

    List<T> findAll();

    void add(T entity);

    List<T> findById(D id);

    void update(T entity);

    void delete(D id);
}
