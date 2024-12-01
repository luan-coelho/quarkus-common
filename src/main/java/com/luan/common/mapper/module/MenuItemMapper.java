package com.luan.common.mapper.module;

import com.luan.common.dto.module.MenuItemCreateDto;
import com.luan.common.dto.module.MenuItemResponseDto;
import com.luan.common.mapper.BaseMapper;
import com.luan.common.mapper.QuarkusMappingConfig;
import com.luan.common.model.module.MenuItem;
import jakarta.transaction.Transactional;
import org.mapstruct.*;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemMapper extends BaseMapper<MenuItem, MenuItemResponseDto> {

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "subItems", expression = "java(source.getSubItems())")
    @Mapping(target = "id", ignore = true)
    @Override
    void copyProperties(MenuItem source, @MappingTarget MenuItem target);

    MenuItem toEntity(MenuItemResponseDto dto);

    @Transactional
    @Override
    MenuItemResponseDto toDto(MenuItem target);

    MenuItem toEntity(MenuItemCreateDto dto);

}
