package com.luan.common.dto.module;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.luan.common.model.module.Module}
 */
public record ModuleResponseDto(
        UUID id,
        String name,
        List<MenuItemResponseDto> menuItems,
        List<ModuleUserResponseDto> users,
        boolean active) implements Serializable {

}

