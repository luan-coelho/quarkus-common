package com.luan.common.service;

import java.util.Collection;

public interface Service<T, ID> {

    T save(T entity);

    T findById(ID id);

    Collection<T> findAll();

    T updateById(T entity, ID id);

    void deleteById(ID id);

    boolean existsById(ID id);

}

