package com.luan.common.mapper.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.luan.common.dto.user.CreateNaturalPersonDto;
import com.luan.common.dto.user.naturalperson.NaturalPersonResponseDto;
import com.luan.common.mapper.BaseMapper;
import com.luan.common.mapper.QuarkusMappingConfig;
import com.luan.common.model.user.NaturalPerson;
import com.luan.common.util.pagination.DataPagination;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NaturalPersonMapper extends BaseMapper<NaturalPerson, NaturalPersonResponseDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Override
    void copyProperties(NaturalPerson source, @MappingTarget NaturalPerson target);

    @Override
    NaturalPersonResponseDto toDto(NaturalPerson targetClass);

    @Override
    DataPagination<NaturalPersonResponseDto> toDto(DataPagination<NaturalPerson> target);

    NaturalPerson toEntity(CreateNaturalPersonDto source);

}
