package com.epam.esm.service;

import java.util.List;

public interface CrdService<T> {

    public List<T> findAll(int offset, int limit);

    public T findById(long id);

    public T create(T entity);

    public long delete(long id);

    public long findNumberOfEntities();

}
