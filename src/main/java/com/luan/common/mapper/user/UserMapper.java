package com.luan.common.mapper.user;

import com.luan.common.dto.user.CreateAdminUserDto;
import com.luan.common.dto.user.UserResponseDto;
import com.luan.common.mapper.BaseMapper;
import com.luan.common.mapper.QuarkusMappingConfig;
import com.luan.common.model.user.User;
import com.luan.common.util.pagination.DataPagination;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<User, UserResponseDto> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Override
    void copyProperties(User source, @MappingTarget User target);

    @Override
    UserResponseDto toDto(User targetClass);

    @Override
    DataPagination<UserResponseDto> toDto(DataPagination<User> target);

    User toEntity(CreateAdminUserDto source);

}
