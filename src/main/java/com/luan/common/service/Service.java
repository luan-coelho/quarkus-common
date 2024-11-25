package com.luan.common.service;

import com.luan.common.util.audit.Revision;
import com.luan.common.util.audit.RevisionComparison;
import com.luan.common.util.pagination.DataPagination;
import com.luan.common.util.pagination.Pageable;

import java.util.Collection;
import java.util.List;

public interface Service<T, DTO, UUID> {

    DTO save(T entity);

    DTO findById(UUID id);

    Collection<DTO> findAll();

    DataPagination<DTO> findAll(Pageable pageable);

    DTO updateById(UUID id, T entity);

    void deleteById(UUID id);

    boolean existsById(UUID id);

    List<Revision<DTO>> findAllRevisions(UUID id);

    RevisionComparison compareWithPreviousRevision(UUID entityId, Integer revisionId);

}

