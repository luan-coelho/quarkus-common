package com.luan.common.handle;

import com.luan.common.handle.rest.response.ErrorResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class HandleExceptionMapper<T extends Exception> {

    @Context
    HttpServerRequest request;

    abstract String getTitle();

    abstract int getStatus();

    public ErrorResponse buildResponse(T exception, HttpServerRequest request) throws URISyntaxException {
        URI type = new URI("about:blank");
        String title = getTitle();
        int status = getStatus();
        String detail = exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage();
        URI instance = new URI(request.path());
        return new ErrorResponse(type, title, status, detail, instance);
    }

    public Response toResponse(T exception) {
        try {
            ErrorResponse errorResponse = buildResponse(exception, request);
            return Response.status(errorResponse.getStatus()).entity(errorResponse).build();
        } catch (URISyntaxException e) {
            return Response.status(HttpResponseStatus.INTERNAL_SERVER_ERROR.code()).build();
        }
    }

}
