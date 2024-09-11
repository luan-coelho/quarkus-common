package com.luan.common.resource;

import com.luan.common.model.Car;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@Path("/cars")
public class CarResource {

    @GET
    public String list() {
        return "list";
    }

    @POST
    public String create(@Valid Car car) {
        return null;
    }

}