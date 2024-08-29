package com.luan.common.handle;

import com.luan.common.handle.rest.response.ErrorResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public abstract class HandleExceptionMapper<T extends Exception> {

    @Context
    HttpServerRequest request;

    abstract String getTitle();

    abstract int getStatus();

    public ErrorResponse getExceptionResponse(T exception, HttpServerRequest request) {
        return switch (exception) {
            case Exception -> getGenericExceptionResponse(exception, request);
            default -> getGenericExceptionResponse(exception, request);
        };
    }

    public Response toResponse(T exception) {
        ErrorResponse errorResponse = buildResponse(exception, request);
        return Response.status(errorResponse.getStatus()).entity(errorResponse).build();
    }

    public ErrorResponse buildResponse(T exception, HttpServerRequest request) {
        String type = "about:blank";
        String title = getTitle();
        int status = getStatus();
        String detail = exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage();
        String instance = request.path();
        return new ErrorResponse(type, title, status, detail, instance);
    }

    private ErrorResponse getGenericExceptionResponse(T exception, HttpServerRequest request) {
        return buildResponse(exception, request);
    }

}
