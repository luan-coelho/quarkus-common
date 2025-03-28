package com.luan.common.repository;

import com.luan.common.util.pagination.*;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @param <T>  Entity type
 * @param <ID> Entity ID type
 */
public class BaseRepository<T, ID> implements PanacheRepositoryBase<T, ID> {

    public DataPagination<T> listAll(Pageable pageable) {
        QueryAndParameters qp = PanacheFilterUtils.buildQueryFromFiltersAndSort(
                pageable.getFilters(),
                pageable.getSort(),
                getEntityClass());
        PanacheQuery<T> panacheQuery = !qp.query().isEmpty()
                ? find(qp.query(), qp.parameters())
                : findAll();
        return buildDataPagination(pageable, panacheQuery);
    }

    public List<T> findByIds(List<ID> ids) {
        return list("id in ?1", ids);
    }

    public boolean existsById(ID id) {
        return count("id", id) > 0;
    }

    public void desactiveById(ID id) {
        update("active = false where id = ?1", id);
    }

    public void activeById(ID id) {
        update("active = true where id = ?1", id);
    }

    public DataPagination<T> buildDataPagination(Pageable pageable, PanacheQuery<T> panacheQuery) {
        panacheQuery.page(Page.of(pageable.getPage() - 1, pageable.getSize()));
        List<T> list = panacheQuery.list();
        Pagination pagination = buildPaginationFromPageable(pageable, panacheQuery);
        pagination.setItemsOnPage(list.size());
        return new DataPagination<>(list, pagination);
    }

    private Pagination buildPaginationFromPageable(Pageable pageable, PanacheQuery<T> panacheQuery) {
        long totalItems = panacheQuery.count();
        int itemsOnPage = pageable.getSize();
        long totalPages = (long) Math.ceil((double) totalItems / pageable.getSize());
        return new Pagination(pageable.getPage(), itemsOnPage, totalPages, totalItems);
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEntityClass() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
