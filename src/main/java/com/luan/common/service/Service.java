package com.luan.common.service;

import com.luan.common.util.audit.FieldChange;
import com.luan.common.util.audit.Revision;
import com.luan.common.util.audit.RevisionComparison;
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

    List<Revision<T>> findAllRevisions(UUID id);

    RevisionComparison compareWithPreviousRevision(UUID entityId, Integer revisionId);

}

