package com.luan.common.mapper;

import com.luan.common.handle.rest.response.ConstraintProblemDetails;
import com.luan.common.handle.rest.response.ProblemDetails;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(config = QuarkusMappingConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ErrorResponseMapper {

    ConstraintProblemDetails copyProperties(ProblemDetails errorResponse);

}
