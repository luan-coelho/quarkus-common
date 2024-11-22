package com.luan.common.service.module;

import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.mapper.module.ModuleMapper;
import com.luan.common.model.module.Module;
import com.luan.common.repository.module.ModuleRepository;
import com.luan.common.service.BaseService;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class ModuleService extends BaseService<Module, ModuleResponseDto, UUID, ModuleRepository, ModuleMapper> {

    protected ModuleService() {
        super(Module.class);
    }

}
