package com.luan.common.controller.module;

import com.luan.common.controller.BaseController;
import com.luan.common.dto.module.MenuItemOrder;
import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.model.module.Module;
import com.luan.common.service.module.ModuleService;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.UUID;

@Path("/modules")
public class ModuleController extends BaseController<Module, ModuleResponseDto, UUID, ModuleService> {

    @Path("/{id}/add-user/{userId}")
    @PATCH
    public Response addUser(@PathParam("id") UUID id, @PathParam("userId") UUID userId) {
        return Response.ok(getService().addUser(id, userId)).build();
    }

    @Path("/{id}/add-menu-item/{menuItemId}")
    @PATCH
    public Response addMenuItem(@PathParam("id") UUID id, @PathParam("menuItemId") UUID menuItemId) {
        return Response.ok(getService().addMenuItem(id, menuItemId)).build();
    }

    @Path("/{id}/remove-menu-item/{menuItemId}")
    @PATCH
    public Response removeMenuItem(@PathParam("id") UUID id, @PathParam("menuItemId") UUID menuItemId) {
        return Response.ok(getService().removeMenuItem(id, menuItemId)).build();
    }

    @Path("/{id}/update-menu-items-order")
    @PATCH
    public Response updateMenuItemsOrder(@PathParam("id") UUID id, List<MenuItemOrder> menuItemsOrder) {
        return Response.ok(getService().updateMenuItemsOrder(id, menuItemsOrder)).build();
    }

}
