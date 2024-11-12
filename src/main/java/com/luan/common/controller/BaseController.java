package com.luan.common.controller;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.service.Service;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

@SuppressWarnings({"CdiInjectionPointsInspection", "RestParamTypeInspection"})
public abstract class BaseController<T extends BaseEntity, UUID, S extends Service<T, UUID>> {

    @Inject
    S service;

    @GET
    public Response getAll() {
        return Response.ok(this.service.findAll()).build();
    }

    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") UUID id) {
        return Response.ok(service.findById(id)).build();
    }

    @POST
    public Response save(T entity) {
        return Response.ok(service.save(entity)).build();
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

}
