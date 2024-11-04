package com.luan.common.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/company")
public class CompanyController {

    @GET
    public Response getAll() {
        return Response.ok("Empresa").build();
    }

}
