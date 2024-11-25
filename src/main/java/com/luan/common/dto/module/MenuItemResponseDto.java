package com.luan.common.dto.module;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.luan.common.model.module.MenuItem}
 */
public record MenuItemResponseDto(
        UUID id,
        String label,
        String route,
        String icon,
        Integer position,
        MenuItemResponseDto parent,
        boolean visible,
        boolean active) implements Serializable {

}