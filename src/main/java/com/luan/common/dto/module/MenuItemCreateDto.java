package com.luan.common.dto.module;

import java.util.List;

/**
 * DTO for {@link com.luan.common.model.module.MenuItem}
 */
public record MenuItemCreateDto(
        String label,
        String route,
        String icon,
        Integer position,
        List<MenuItemResponseDto> subItems
) {
}
