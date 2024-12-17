package com.luan.common.dto.module;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.luan.common.model.module.Module}
 */
public record ModuleResponseDto(
        UUID id,
        String name,
        List<MenuItemResponseDto> menuItems,
        String menuItemsOrder,
        List<ModuleUserResponseDto> users,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean active) implements Serializable {

}

