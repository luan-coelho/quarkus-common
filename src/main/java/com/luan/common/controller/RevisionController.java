package com.luan.common.controller;

import com.luan.common.model.user.BaseEntity;
import com.luan.common.service.Service;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

public interface RevisionController<T extends BaseEntity, DTO, UUID, S extends Service<T, DTO, UUID>> {

    S getService();

    @Path("/{id}/revisions")
    @GET
    default Response findAllRevisions(@PathParam("id") UUID id) {
        return Response.ok(getService().findAllRevisions(id)).build();
    }

    @Path("/{id}/revisions/{revision}/compare")
    @GET
    default Response compareWithRevision(@PathParam("id") UUID id, @PathParam("revision") Integer revision) {
        return Response.ok(getService().compareWithPreviousRevision(id, revision)).build();
    }

    @Path("/{id}/revisions/comparisons")
    @GET
    default Response findAllRevisionsComparisons(@PathParam("id") UUID id) {
        return Response.ok(getService().findAllRevisionsComparisons(id)).build();
    }

} 