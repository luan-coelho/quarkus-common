package com.luan.common.util.pagination;

import jakarta.ws.rs.QueryParam;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class Pageable {

    @QueryParam("page")
    private int page = 1;

    @QueryParam("size")
    private int size = Pagination.STANDARD_PAGE_SIZE;

    @QueryParam("sort")
    private String sort = "id:asc";

    @QueryParam("filters")
    private String filters;

}
