package com.luan.common.mapper.user;

import com.luan.common.dto.user.UserResponseDto;
import com.luan.common.mapper.BaseMapper;
import com.luan.common.mapper.QuarkusMappingConfig;
import com.luan.common.model.user.User;
import org.mapstruct.*;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<User> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Override
    void copyProperties(User source, @MappingTarget User target);

    @Override
    UserResponseDto toDto(User targetClass);

}
