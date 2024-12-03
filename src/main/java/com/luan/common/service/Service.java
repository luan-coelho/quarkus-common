package com.luan.common.service;

import com.luan.common.util.audit.Revision;
import com.luan.common.util.audit.RevisionComparison;
import com.luan.common.util.pagination.DataPagination;
import com.luan.common.util.pagination.Pageable;

import java.util.Collection;
import java.util.List;

public interface Service<T, DTO, UUID> {

    T save(T entity);

    DTO saveAndReturnDto(T entity);

    T findById(UUID id);

    DTO findByIdAndReturnDto(UUID id);

    Collection<T> findAll();

    Collection<DTO> findAllAndReturnDto();

    DataPagination<T> findAll(Pageable pageable);

    DataPagination<DTO> findAllAndReturnDto(Pageable pageable);

    T updateById(UUID id, T entity);

    DTO updateByIdAndReturnDto(UUID id, T entity);

    void deleteById(UUID id);

    void deleteAll();

    T activateById(UUID id);

    DTO activateByIdAndReturnDto(UUID id);

    T disableById(UUID id);

    DTO disableByIdAndReturnDto(UUID id);

    boolean existsById(UUID id);

    List<Revision<T>> findAllRevisions(UUID id);

    RevisionComparison compareWithPreviousRevision(UUID entityId, Integer revisionId);

}

