package com.luan.common.controller.module;

import com.luan.common.controller.BaseController;
import com.luan.common.dto.module.MenuItemResponseDto;
import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.mapper.module.MenuItemMapper;
import com.luan.common.model.module.MenuItem;
import com.luan.common.model.module.Module;
import com.luan.common.service.module.MenuItemService;
import com.luan.common.service.module.ModuleService;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.UUID;

@Path("/menu-item")
public class MenuItemController extends BaseController<MenuItem, MenuItemResponseDto, UUID, MenuItemService, MenuItemMapper> {

    @Path("/{id}/add-sub-item/{subItemId}")
    @PATCH
    public Response addSubItem(@PathParam("id") UUID id, @PathParam("subItemId") UUID subItemId) {
        return Response.ok(getService().addSubItem(id, subItemId)).build();
    }

}
