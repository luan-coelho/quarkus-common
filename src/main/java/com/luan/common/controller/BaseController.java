package com.luan.common.controller;

import com.luan.common.mapper.BaseMapper;
import com.luan.common.model.user.BaseEntity;
import com.luan.common.service.Service;
import com.luan.common.util.pagination.DataPagination;
import com.luan.common.util.pagination.Pageable;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
@Produces(MediaType.APPLICATION_JSON)
@SuppressWarnings({"CdiInjectionPointsInspection", "RestParamTypeInspection"})
public abstract class BaseController<T extends BaseEntity, DTO, UUID, S extends Service<T, UUID>, M extends BaseMapper<T, DTO>> {

    @Inject
    S service;

    @Inject
    M mapper;

    @GET
    public Response getAll(Pageable pageable) {
        DataPagination<T> dataPagination = service.findAll(pageable);
        DataPagination<DTO> dto = mapper.toDto(dataPagination);
        return Response.ok(dto).build();
    }

    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") UUID id) {
        DTO dto = mapper.toDto(service.findById(id));
        return Response.ok(dto).build();
    }

    @POST
    public Response save(T entity) {
        service.save(entity);
        DTO dto = mapper.toDto(entity);
        return Response.status(Response.Status.CREATED).entity(dto).build();
    }

    @Path("/{id}")
    @PUT
    public Response updateById(T entity, @PathParam("id") UUID id) {
        DTO dto = mapper.toDto(service.updateById(id, entity));
        return Response.ok(dto).build();
    }

    @Path("/{id}")
    @DELETE
    public Response deleteById(@PathParam("id") UUID id) {
        service.deleteById(id);
        return Response.noContent().build();
    }

    @Path("/{id}/revisions")
    @GET
    public Response findAllRevisions(UUID id) {
        return Response.ok(service.findAllRevisions(id)).build();
    }

    @Path("/{id}/revisions/{revision}/compare")
    @GET
    public Response compareWithRevision(UUID id, Integer revision) {
        return Response.ok(service.compareWithPreviousRevision(id, revision)).build();
    }

}
