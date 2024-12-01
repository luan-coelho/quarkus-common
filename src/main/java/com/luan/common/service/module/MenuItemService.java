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

import java.util.List;
import java.util.UUID;

@Slf4j
@ApplicationScoped
public class MenuItemService extends BaseService<MenuItem, MenuItemResponseDto, UUID, MenuItemRepository, MenuItemMapper> {

    protected MenuItemService() {
        super(MenuItem.class);
    }

    @Transactional
    public MenuItem save(MenuItem entity) {
        validateSubItems(entity);
        return super.save(entity);
    }

    @Transactional
    @Override
    public MenuItemResponseDto saveAndReturnDto(MenuItem entity) {
        save(entity);
        return getMapper().toDto(entity);
    }

    @Transactional
    public MenuItem updateById(UUID id, MenuItem entity) {
        MenuItem menuItem = getRepository()
                .findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Item de menu não encontrado"));
        validateSubItems(entity);
        getMapper().copyProperties(entity, menuItem);
        return update(menuItem);
    }

    @Transactional
    public MenuItemResponseDto addSubItem(UUID id, UUID subItemId) {
        MenuItem menuItem = getRepository()
                .findByIdOptional(id)
                .orElseThrow(() -> new NotFoundException("Item de menu não encontrado"));
        MenuItem subItem = getRepository().findByIdOptional(subItemId)
                .orElseThrow(() -> new NotFoundException("Item filho não encontrado"));
        menuItem.getSubItems().add(subItem);
        return getMapper().toDto(update(menuItem));
    }

    @Transactional
    public void validateSubItems(MenuItem entity) {
        List<MenuItem> subItems = entity.getSubItems();

        if (subItems == null) {
            return;
        }

        for (int i = 0; i < subItems.size(); i++) {
            MenuItem subItem = subItems.get(i);
            if (subItem.getId() != null) {
                MenuItem submenu = getRepository()
                        .findByIdOptional(subItem.getId())
                        .orElseThrow(() -> new NotFoundException("Item filho não encontrado"));
                subItems.set(i, submenu);
            } else {
                subItems.set(i, null);
            }
        }
    }

}
