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

    private int currentPage;
    private int itemsPerPage = STANDARD_PAGE_SIZE;
    private long totalPages;
    private long totalItems;

    public static final int STANDARD_PAGE_SIZE = 25;

    public Pagination(int currentPage, long totalPages, long totalItems) {
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

}
