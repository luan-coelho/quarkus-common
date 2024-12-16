package com.luan.common.controller.user;

import com.luan.common.controller.BaseController;
import com.luan.common.dto.user.UserResponseDto;
import com.luan.common.model.user.User;
import com.luan.common.service.module.ModuleService;
import com.luan.common.service.user.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/users")
public class UserController extends BaseController<User, UserResponseDto, UUID, UserService> {

    @Inject
    ModuleService moduleService;

    @GET
    @Path("/{id}/modules")
    public Response getUserModules(@PathParam("id") UUID id) {
        return Response.ok(moduleService.getModulesByUserId(id)).build();
    }

}
