package com.luan.common.mapper.module;

import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.mapper.BaseMapper;
import com.luan.common.mapper.QuarkusMappingConfig;
import com.luan.common.model.module.Module;
import org.mapstruct.*;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModuleMapper extends BaseMapper<Module> {

    @Mapping(target = "id", ignore = true)
    @Override
    void copyProperties(Module source, @MappingTarget Module target);

    Module toEntity(ModuleResponseDto moduleResponseDto);

    @Override
    ModuleResponseDto toDto(Module module);

}
