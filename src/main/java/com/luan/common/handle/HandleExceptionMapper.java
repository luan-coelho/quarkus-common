package com.luan.common.handle;

import com.luan.common.handle.rest.response.ErrorResponse;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

public abstract class HandleExceptionMapper<T extends Exception> {

    @Context
    HttpServerRequest request;

    protected String getType() {
        return "about:blank";
    }

    abstract String getTitle();

    abstract int getStatus();

    public Response toResponse(T exception) {
        ErrorResponse errorResponse = buildResponse(exception, request);
        return Response.status(errorResponse.getStatus()).entity(errorResponse).build();
    }

    protected ErrorResponse buildResponse(T exception, HttpServerRequest request) {
        String type = getType();
        String title = getTitle();
        int status = getStatus();
        String detail = exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage();
        String instance = request.path();
        return new ErrorResponse(type, title, status, detail, instance);
    }

}
