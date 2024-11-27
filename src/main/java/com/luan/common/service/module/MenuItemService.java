package com.luan.common.service.module;

import com.luan.common.dto.module.MenuItemResponseDto;
import com.luan.common.mapper.module.MenuItemMapper;
import com.luan.common.model.module.MenuItem;
import com.luan.common.repository.module.MenuItemRepository;
import com.luan.common.service.BaseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
@ApplicationScoped
public class MenuItemService extends BaseService<MenuItem, MenuItemResponseDto, UUID, MenuItemRepository, MenuItemMapper> {

    protected MenuItemService() {
        super(MenuItem.class);
    }

    @Transactional
    public MenuItem save(MenuItem entity) {
        validateParent(entity);
        return super.save(entity);
    }

    @Transactional
    public MenuItem updateById(UUID id, MenuItem entity) {
        MenuItem menuItem = getRepository()
                .findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Menu não encontrado"));
        validateParent(entity);
        getMapper().copyProperties(entity, menuItem);
        return update(menuItem);
    }

    @Transactional
    public void validateParent(MenuItem entity) {
        if (entity.getParent() != null && entity.getParent().getId() != null) {
            MenuItem parent = getRepository()
                    .findByIdOptional(entity.getParent().getId())
                    .orElseThrow(() -> new NotFoundException("Menu pai não encontrado"));
            entity.setParent(parent);
        } else {
            entity.setParent(null);
        }
    }

}
