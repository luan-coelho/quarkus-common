package com.luan.common.controller.module;

import com.luan.common.controller.BaseController;
import com.luan.common.dto.module.MenuItemResponseDto;
import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.mapper.module.MenuItemMapper;
import com.luan.common.model.module.MenuItem;
import com.luan.common.model.module.Module;
import com.luan.common.service.module.MenuItemService;
import com.luan.common.service.module.ModuleService;
import jakarta.ws.rs.Path;

import java.util.UUID;

@Path("/menu-item")
public class MenuItemController extends BaseController<MenuItem, MenuItemResponseDto, UUID, MenuItemService, MenuItemMapper> {

}
