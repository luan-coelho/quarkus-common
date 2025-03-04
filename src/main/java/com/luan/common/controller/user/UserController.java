package com.luan.common.controller.user;

import com.itextpdf.styledxmlparser.jsoup.internal.StringUtil;
import com.luan.common.controller.BaseController;
import com.luan.common.dto.user.CreateAdminUserDto;
import com.luan.common.dto.user.UserResponseDto;
import com.luan.common.model.user.User;
import com.luan.common.service.module.ModuleService;
import com.luan.common.service.user.UserService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.Map;
import java.util.UUID;

@Path("/users")
public class UserController extends BaseController<User, UserResponseDto, UUID, UserService> {

    @Inject
    ModuleService moduleService;

    @Path("/admin")
    @POST
    public Response save(CreateAdminUserDto dto) {
        User savedUser = service.save(dto);
        return Response.ok(savedUser).build();
    }

    @Path("/admin/exists/cpf/{cpf}")
    @GET
    public Response existsByCpf(@PathParam("cpf") String cpf) {
        if (StringUtil.isBlank(cpf)) {
            throw new IllegalArgumentException("CPF não pode ser nulo");
        }
        boolean exists = service.existsByCpf(cpf);
        Map<String, Boolean> response = Map.of("exists", exists);
        return Response.ok(response).build();
    }

    @Path("/admin/exists/email/{email}")
    @GET
    public Response existsByEmail(@PathParam("email") String email) {
        if (StringUtil.isBlank(email)) {
            throw new IllegalArgumentException("Email não pode ser nulo");
        }
        boolean exists = service.existsByEmail(email);
        Map<String, Boolean> response = Map.of("exists", exists);
        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}/modules")
    public Response getUserModules(@PathParam("id") UUID id) {
        return Response.ok(moduleService.getModulesByUserId(id)).build();
    }

}
