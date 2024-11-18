package com.luan.common.mapper.user;

import com.luan.common.mapper.BaseMapper;
import com.luan.common.mapper.QuarkusMappingConfig;
import com.luan.common.model.user.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper extends BaseMapper<Address> {

    void copyProperties(Address source, @MappingTarget Address target);

}
