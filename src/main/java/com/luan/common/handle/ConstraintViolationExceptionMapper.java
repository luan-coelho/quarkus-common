package com.luan.common.handle;

import com.luan.common.handle.rest.response.ConstraintErrorResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper extends HandleExceptionMapper<ConstraintViolationException>
        implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public String getTitle() {
        return HttpResponseStatus.BAD_REQUEST.reasonPhrase();
    }

    @Override
    public int getStatus() {
        return HttpResponseStatus.BAD_REQUEST.code();
    }

    @Override
    protected String getDetail() {
        return "Validation failed";
    }

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ConstraintErrorResponse constraintErrorResponse = this.mapper.copyProperties(buildResponse(exception, uriInfo));
        constraintErrorResponse.addErrors(exception.getConstraintViolations());
        return getResponse(constraintErrorResponse);
    }

}
