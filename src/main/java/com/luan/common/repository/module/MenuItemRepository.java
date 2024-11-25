package com.luan.common.repository.module;

import com.luan.common.model.module.MenuItem;
import com.luan.common.repository.Repository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class MenuItemRepository extends Repository<MenuItem, UUID> {

}
