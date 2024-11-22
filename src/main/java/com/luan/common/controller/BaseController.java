package com.luan.common.controller;

import com.luan.common.service.Service;
import com.luan.common.util.pagination.Pageable;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@SuppressWarnings({"CdiInjectionPointsInspection", "RestParamTypeInspection"})
public abstract class BaseController<T, DTO, UUID, S extends Service<T, DTO, UUID>> {

    @Inject
    S service;

    @GET
    public Response getAll(Pageable pageable) {
        return Response.ok(service.findAll(pageable)).build();
    }

    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") UUID id) {
        return Response.ok(service.findById(id)).build();
    }

    @POST
    public Response save(T entity) {
        return Response.status(Response.Status.CREATED).entity(service.save(entity)).build();
    }

    @Path("/{id}")
    @PUT
    public Response updateById(T entity, @PathParam("id") UUID id) {
        return Response.ok(service.updateById(entity, id)).build();
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
