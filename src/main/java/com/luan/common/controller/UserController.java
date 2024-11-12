package com.luan.common.controller;

import com.luan.common.model.user.User;
import com.luan.common.service.UserService;
import jakarta.ws.rs.Path;

import java.util.UUID;

@Path("/users")
public class UserController extends BaseController<User, UUID, UserService> {

}
