package com.luan.common.controller.user;

import com.luan.common.controller.BaseController;
import com.luan.common.dto.user.NaturalPersonResponseDto;
import com.luan.common.model.user.NaturalPerson;
import com.luan.common.service.user.NaturalPersonService;
import jakarta.ws.rs.Path;

import java.util.UUID;

@Path("/users/natural-persons")
public class NaturalPersonController
        extends BaseController<NaturalPerson, NaturalPersonResponseDto, UUID, NaturalPersonService> {

}
