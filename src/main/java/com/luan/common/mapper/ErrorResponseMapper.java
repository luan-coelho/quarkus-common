package com.luan.common.mapper;

import com.luan.common.handle.rest.response.ErrorResponse;
import com.luan.common.handle.rest.response.ValidationErrorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ErrorResponseMapper {

    ValidationErrorResponse copyProperties(ErrorResponse errorResponse);

    ValidationErrorResponse copyProperties(ValidationErrorResponse errorResponse);

}
