package com.luan.common.mapper.module;

import com.luan.common.dto.module.MenuItemCreateDto;
import com.luan.common.dto.module.MenuItemResponseDto;
import com.luan.common.mapper.BaseMapper;
import com.luan.common.mapper.QuarkusMappingConfig;
import com.luan.common.model.module.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemMapper extends BaseMapper<MenuItem, MenuItemResponseDto> {

    @Mapping(target = "id", ignore = true)
    @Override
    void copyProperties(MenuItem source, @MappingTarget MenuItem target);

    MenuItem toEntity(MenuItemResponseDto dto);

    @Override
    MenuItemResponseDto toDto(MenuItem target);

    MenuItem toEntity(MenuItemCreateDto dto);

}
