package com.luan.common.util.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public final class Pagination {

    private int itemsOnPage;
    private long totalItems;
    private int itemsPerPage = STANDARD_PAGE_SIZE;
    private int currentPage;
    private long totalPages;

    public static final int STANDARD_PAGE_SIZE = 25;

    public Pagination(int currentPage, int itemsPerPage, long totalPages, long totalItems) {
        this.currentPage = currentPage;
        this.itemsPerPage = itemsPerPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

}
