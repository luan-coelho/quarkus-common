package com.luan.common.handle;

import com.luan.common.handle.rest.response.ConstraintErrorResponse;
import com.luan.common.handle.rest.response.ErrorResponse;
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
    public Response toResponse(ConstraintViolationException exception) {
        ErrorResponse errorResponse = buildResponse(exception, request);
        ConstraintErrorResponse constraintErrorResponse = ((ConstraintErrorResponse) errorResponse);
//        errorResponse.addErrors(exception.getConstraintViolations());
        return Response.status(errorResponse.getStatus()).entity(errorResponse).build();
    }

}
