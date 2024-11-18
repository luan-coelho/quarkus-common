package com.luan.common.dto.module;

import com.luan.common.dto.user.UserResponseDto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.luan.common.model.module.Module}
 */
public record ModuleResponseDto(UUID id,
                                String name,
                                List<MenuItemResponseDto> menuItems,
                                List<UserResponseDto> users,
                                boolean active) implements Serializable {

}