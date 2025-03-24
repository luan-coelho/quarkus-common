package com.luan.common.controller;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.service.Service;
import com.luan.common.util.pagination.Pageable;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

public interface PaginationController<T extends BaseEntity, DTO, UUID, S extends Service<T, DTO, UUID>> {

    S getService();

    @GET
    default Response getAllWithPagination(Pageable pageable) {
        return Response.ok(getService().findAllAndReturnDto(pageable)).build();
    }

    @GET
    @Path("/all")
    default Response getAll() {
        return Response.ok(getService().findAllAndReturnDto()).build();
    }

} 