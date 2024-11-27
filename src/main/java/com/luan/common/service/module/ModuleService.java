package com.luan.common.service.module;

import com.luan.common.dto.module.MenuItemCreateDto;
import com.luan.common.dto.module.MenuItemResponseDto;
import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.mapper.module.MenuItemMapper;
import com.luan.common.mapper.module.ModuleMapper;
import com.luan.common.model.module.MenuItem;
import com.luan.common.model.module.Module;
import com.luan.common.repository.module.ModuleRepository;
import com.luan.common.service.BaseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class ModuleService extends BaseService<Module, ModuleResponseDto, UUID, ModuleRepository, ModuleMapper> {

    @Inject
    MenuItemMapper menuItemMapper;

    protected ModuleService() {
        super(Module.class);
    }

    public MenuItemResponseDto createMenuItem(UUID moduleId, MenuItemCreateDto dto) {
        ModuleResponseDto moduleDto = findById(moduleId);
        Module module = getMapper().toEntity(moduleDto);
        MenuItem entity = menuItemMapper.toEntity(dto);
        module.getMenuItems().add(entity);
        updateById(moduleId, module);
        return menuItemMapper.toDto(entity);
    }

}
