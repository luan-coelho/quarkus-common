package com.luan.common.mapper.module;

import com.luan.common.dto.module.ModuleResponseDto;
import com.luan.common.mapper.BaseMapper;
import com.luan.common.mapper.QuarkusMappingConfig;
import com.luan.common.model.module.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ModuleMapper extends BaseMapper<Module, ModuleResponseDto> {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Override
    void copyProperties(Module source, @MappingTarget Module target);

    Module toEntity(ModuleResponseDto moduleResponseDto);

    @Override
    ModuleResponseDto toDto(Module module);

}
