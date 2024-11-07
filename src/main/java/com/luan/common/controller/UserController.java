package com.luan.common.controller;

import com.luan.common.model.user.User;
import com.luan.common.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/users")
public class UserController {

    @Inject
    UserService service;

    @GET
    public Response getAll() {
        return Response.ok(this.service.findAll()).build();
    }

    @Path("/{id}")
    @GET
    public Response getById(@PathParam("id") UUID id) {
        User user = service.findById(id);
        return Response.ok(user).build();
    }

}
