package com.luan.common.dto.module;

import java.util.List;
import java.util.UUID;

public record AddMenuItemsToModuleDto(
        List<UUID> menuItemIds
) {

}
