package com.luan.common.dto.module;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.luan.common.model.module.MenuItem}
 */
public record MenuItemResponseDto(
        UUID id,
        String label,
        String description,
        String route,
        String icon,
        Integer position,
        List<MenuItemResponseDto> subItems,
        boolean active) implements Serializable {

}