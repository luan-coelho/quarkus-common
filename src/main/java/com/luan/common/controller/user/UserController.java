package com.luan.common.controller.user;

import com.luan.common.controller.BaseController;
import com.luan.common.dto.user.UserResponseDto;
import com.luan.common.mapper.user.UserMapper;
import com.luan.common.model.user.User;
import com.luan.common.service.user.UserService;
import jakarta.ws.rs.Path;

import java.util.UUID;

@Path("/api/users")
public class UserController extends BaseController<User, UserResponseDto, UUID, UserService, UserMapper> {

}
