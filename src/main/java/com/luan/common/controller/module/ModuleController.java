package com.luan.common.controller.module;

import com.luan.common.controller.BaseController;
import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.model.module.Module;
import com.luan.common.service.module.ModuleService;
import jakarta.ws.rs.Path;

import java.util.UUID;

@Path("/module")
public class ModuleController extends BaseController<Module, ModuleResponseDto, UUID, ModuleService> {

}
