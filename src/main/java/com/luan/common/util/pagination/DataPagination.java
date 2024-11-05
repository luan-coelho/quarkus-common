package com.luan.common.util.pagination;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public final class DataPagination<T> {

    private List<T> data;
    private Pagination pagination;

    public DataPagination() {
        this.pagination = new Pagination();
    }

}
