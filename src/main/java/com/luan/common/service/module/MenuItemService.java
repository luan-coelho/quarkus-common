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
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;

@ApplicationScoped
public class MenuItemService extends BaseService<MenuItem, MenuItemResponseDto, UUID, MenuItemRepository, MenuItemMapper> {

    protected MenuItemService() {
        super(MenuItem.class);
    }

    @Transactional
    @Override
    public MenuItemResponseDto save(MenuItem entity) {
        if (entity.getParent() != null && entity.getParent().getId() != null) {
            MenuItem parent = getRepository()
                    .findByIdOptional(entity.getParent().getId())
                    .orElseThrow(() -> new NotFoundException("Menu pai não encontrado"));
            entity.setParent(parent);
        } else {
            entity.setParent(null);
        }
        return super.save(entity);
    }

}
