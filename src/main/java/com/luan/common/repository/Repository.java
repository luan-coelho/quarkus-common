package com.luan.common.repository;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.util.pagination.DataPagination;
import com.luan.common.util.pagination.Pageable;
import com.luan.common.util.pagination.Pagination;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;

import java.util.List;

public class Repository<T extends BaseEntity, UUID> implements PanacheRepositoryBase<T, UUID> {

    public DataPagination<T> findAll(Pageable pageable) {
        PanacheQuery<T> panacheQuery = findAll();
        return buildDataPagination(pageable, panacheQuery);
    }

    public DataPagination<T> buildDataPagination(Pageable pageable, PanacheQuery<T> panacheQuery) {
        Pagination pagination = buildPaginationFromPageable(pageable, panacheQuery);
        panacheQuery.page(Page.of(pageable.getPage(), pageable.getSize()));
        List<T> list = panacheQuery.list();
        return new DataPagination<>(list, pagination);
    }

    public boolean existsById(UUID id) {
        return count("id", id) > 0;
    }

    private Pagination buildPaginationFromPageable(Pageable pageable, PanacheQuery<T> panacheQuery) {
        long totalItems = panacheQuery.count();
        long totalPages = (long) Math.ceil((double) totalItems / pageable.getSize());
        return new Pagination(pageable.getPage(), totalPages, totalItems);
    }

}
