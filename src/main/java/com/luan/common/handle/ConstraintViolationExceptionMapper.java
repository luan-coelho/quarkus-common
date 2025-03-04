package com.luan.common.handle;

import com.luan.common.handle.rest.response.ConstraintProblemDetails;
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
        return "Requisição inválida";
    }

    @Override
    public int getStatus() {
        return HttpResponseStatus.BAD_REQUEST.code();
    }

    @Override
    protected String getDetail() {
        return "Validação falhou";
    }

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        ConstraintProblemDetails constraintErrorResponse = this.mapper.copyProperties(buildResponse(exception, uriInfo));
        constraintErrorResponse.addErrors(exception.getConstraintViolations());
        return getResponse(constraintErrorResponse);
    }

}
