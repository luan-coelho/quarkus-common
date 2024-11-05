package com.luan.common.controller;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.server.multipart.FormValue;
import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

import java.util.Collection;
import java.util.Map;

@Path("/company")
public class CompanyController {

    @GET
    public Response getAll() {
        return Response.ok("Empresa").build();
    }

    @POST
    public Response create() {
        return Response.ok("Empresa criada").build();
    }

    @Path("/upload-file")
    @POST
    public Response uploadFile(MultipartFormDataInput input) {
        Map<String, Collection<FormValue>> values = input.getValues();
        values.forEach((key, value) -> {
            value.forEach(formValue -> {
                String fileName = formValue.getFileName();
                System.out.println("File Name: " + fileName);
            });
        });
        return Response.ok("Arquivo enviado").build();
    }

}
