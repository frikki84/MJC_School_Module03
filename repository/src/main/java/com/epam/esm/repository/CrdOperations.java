package com.epam.esm.repository;

import java.util.List;

public interface CrdOperations<T> {

    public List<T> findAll(int offset, int limit);

    public T findById(long id);

    public T create(T entity);

    public long delete(long id);

    public Long findNumberOfEntities();

}

