package com.luan.common.repository;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.util.pagination.DataPagination;
import com.luan.common.util.pagination.Pageable;

import java.util.UUID;

public interface DataServiceRepository<T extends BaseEntity> {

    DataPagination<T> findAll(Pageable pageable);

    T findById(UUID id);

    T save(T entity);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    long count();

}
