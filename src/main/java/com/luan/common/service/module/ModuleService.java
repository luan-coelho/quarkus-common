package com.luan.common.service.module;

import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.mapper.module.ModuleMapper;
import com.luan.common.model.module.MenuItem;
import com.luan.common.model.module.Module;
import com.luan.common.model.user.User;
import com.luan.common.repository.module.ModuleRepository;
import com.luan.common.service.BaseService;
import com.luan.common.service.user.UserService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class ModuleService extends BaseService<Module, ModuleResponseDto, UUID, ModuleRepository, ModuleMapper> {

    @Inject
    UserService userService;

    @Inject
    MenuItemService menuItemService;

    protected ModuleService() {
        super(Module.class);
    }

    @Transactional
    @Override
    public Module save(Module entity) {
        return super.save(entity);
    }

    @Transactional
    public Module addUser(UUID moduleId, UUID userId) {
        Module module = findById(moduleId);
        List<User> users = module.getUsers();
        if (users != null && !users.isEmpty()) {
            users.stream().filter(user -> user.getId().equals(userId)).forEach(user -> {
                throw new IllegalArgumentException("Usuário já vinculado ao módulo");
            });
        }
        User user = userService.findById(userId);
        user.getModules().add(module);
        module.getUsers().add(user);
        return update(module);
    }

    @Transactional
    public Module addMenuItem(UUID moduleId, UUID menuItemId) {
        Module module = findById(moduleId);
        List<MenuItem> menuItems = module.getMenuItems();
        if (menuItems != null && !menuItems.isEmpty()) {
            menuItems.stream().filter(item -> item.getId().equals(menuItemId)).forEach(item -> {
                throw new IllegalArgumentException("Item de menu já vinculado ao módulo");
            });
        }
        MenuItem menuItem = menuItemService.findById(menuItemId);
        module.getMenuItems().add(menuItem);
//        Hibernate.initialize(module.getUsers());
        return update(module);
    }

}
