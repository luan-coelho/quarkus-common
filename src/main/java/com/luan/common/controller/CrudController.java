package com.luan.common.controller;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.service.Service;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

public interface CrudController<T extends BaseEntity, DTO, UUID, S extends Service<T, DTO, UUID>> {

    S getService();

    @Path("/{id}")
    @GET
    default Response getById(@PathParam("id") UUID id) {
        return Response.ok(getService().findByIdAndReturnDto(id)).build();
    }

    @POST
    default Response save(T entity) {
        return Response.status(Response.Status.CREATED).entity(getService().saveAndReturnDto(entity)).build();
    }

    @Path("/{id}")
    @PUT
    default Response updateById(@PathParam("id") UUID id, T entity) {
        return Response.ok(getService().updateByIdAndReturnDto(id, entity)).build();
    }

    @Path("/{id}")
    @DELETE
    default Response deleteById(@PathParam("id") UUID id) {
        getService().deleteById(id);
        return Response.noContent().build();
    }

    @Path("/{id}/activate")
    @PATCH
    default Response activateById(@PathParam("id") UUID id) {
        return Response.ok(getService().activateByIdAndReturnDto(id)).build();
    }

    @Path("/{id}/disable")
    @PATCH
    default Response disableById(@PathParam("id") UUID id) {
        return Response.ok(getService().disableByIdAndReturnDto(id)).build();
    }

}
