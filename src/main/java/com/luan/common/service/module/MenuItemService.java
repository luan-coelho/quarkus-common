package com.luan.common.service.module;

import com.luan.common.dto.module.MenuItemResponseDto;
import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.mapper.module.MenuItemMapper;
import com.luan.common.mapper.module.ModuleMapper;
import com.luan.common.model.module.MenuItem;
import com.luan.common.model.module.Module;
import com.luan.common.repository.module.MenuItemRepository;
import com.luan.common.repository.module.ModuleRepository;
import com.luan.common.service.BaseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class MenuItemService extends BaseService<MenuItem, MenuItemResponseDto, UUID, MenuItemRepository, MenuItemMapper> {

    protected MenuItemService() {
        super(MenuItem.class);
    }

}
