package com.luan.common.service.module;

import com.luan.common.mapper.module.ModuleMapper;
import com.luan.common.model.module.Module;
import com.luan.common.repository.module.ModuleRepository;
import com.luan.common.service.BaseService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.UUID;

@ApplicationScoped
public class ModuleService extends BaseService<Module, UUID, ModuleRepository, ModuleMapper> {

    protected ModuleService() {
        super(Module.class);
    }

    @Transactional
    @Override
    public Module save(Module entity) {
        return super.save(entity);
    }

}
