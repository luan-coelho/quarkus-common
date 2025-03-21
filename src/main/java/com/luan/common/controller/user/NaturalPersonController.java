package com.luan.common.controller.user;

import com.luan.common.controller.BaseController;
import com.luan.common.dto.user.UserResponseDto;
import com.luan.common.model.user.User;
import com.luan.common.service.user.UserService;
import jakarta.ws.rs.Path;

import java.util.UUID;

@Path("/natural-persons")
public class NaturalPersonController extends BaseController<User, UserResponseDto, UUID, UserService> {

}
