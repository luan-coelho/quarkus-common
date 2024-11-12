package com.luan.common.service;

import com.luan.common.model.user.AuditRevisionEntity;
import com.luan.common.util.audit.FieldChange;
import com.luan.common.util.pagination.DataPagination;
import com.luan.common.util.pagination.Pageable;

import java.util.Collection;
import java.util.List;

public interface Service<T, UUID> {

    T save(T entity);

    T findById(UUID id);

    Collection<T> findAll();

    DataPagination<T> findAll(Pageable pageable);

    T updateById(T entity, UUID id);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    List<T> listAllRevisions(UUID id);

    List<FieldChange> compareWithPreviousRevision(UUID entityId, int revisionId);

}

