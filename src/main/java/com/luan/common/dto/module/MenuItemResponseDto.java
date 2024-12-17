package com.luan.common.dto.module;

import java.io.Serializable;
import java.time.LocalDateTime;
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
        List<MenuItemResponseDto> subItems,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        boolean active) implements Serializable {

}