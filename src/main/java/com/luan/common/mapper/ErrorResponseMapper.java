package com.luan.common.mapper;

import com.luan.common.handle.rest.response.ConstraintErrorResponse;
import com.luan.common.handle.rest.response.ErrorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ErrorResponseMapper {

    ConstraintErrorResponse copyProperties(ErrorResponse errorResponse);

    ConstraintErrorResponse copyProperties(ConstraintErrorResponse errorResponse);

}
