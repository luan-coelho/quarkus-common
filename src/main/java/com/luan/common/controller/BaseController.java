package com.luan.common.controller;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.service.Service;
import com.luan.common.util.pagination.Pageable;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.Getter;

@Getter
@Produces(MediaType.APPLICATION_JSON)
@SuppressWarnings({"CdiInjectionPointsInspection", "RestParamTypeInspection"})
public abstract class BaseController<T extends BaseEntity, DTO, UUID, S extends Service<T, DTO, UUID>> {

    @Inject
    S service;

    @GET
    public Response getAllWithPagination(Pageable pageable) {
        return Response.ok(service.findAllAndReturnDto(pageable)).build();
    }

    @GET
    @Path("/all")
    public Response getAll() {
        return Response.ok(service.findAllAndReturnDto()).build();
    }

    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") UUID id) {
        return Response.ok(service.findByIdAndReturnDto(id)).build();
    }

    @POST
    public Response save(T entity) {
        return Response.status(Response.Status.CREATED).entity(service.saveAndReturnDto(entity)).build();
    }

    @Path("/{id}")
    @PUT
    public Response updateById(T entity, @PathParam("id") UUID id) {
        return Response.ok(service.updateByIdAndReturnDto(id, entity)).build();
    }

    @Path("/{id}")
    @DELETE
    public Response deleteById(@PathParam("id") UUID id) {
        service.deleteById(id);
        return Response.noContent().build();
    }

    @Path("/{id}/activate")
    @PATCH
    public Response activateById(@PathParam("id") UUID id) {
        return Response.ok(service.activateByIdAndReturnDto(id)).build();
    }

    @Path("/{id}/disable")
    @PATCH
    public Response disableById(@PathParam("id") UUID id) {
        return Response.ok(service.disableByIdAndReturnDto(id)).build();
    }

    @Path("/{id}/revisions")
    @GET
    public Response findAllRevisions(@PathParam("id") UUID id) {
        return Response.ok(service.findAllRevisions(id)).build();
    }

    @Path("/{id}/revisions/{revision}/compare")
    @GET
    public Response compareWithRevision(@PathParam("id") UUID id, @PathParam("revision") Integer revision) {
        return Response.ok(service.compareWithPreviousRevision(id, revision)).build();
    }

}
