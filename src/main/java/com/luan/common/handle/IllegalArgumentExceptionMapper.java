package com.luan.common.handle;

import com.luan.common.handle.rest.response.ErrorResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.SneakyThrows;

@SuppressWarnings("unused")
@Provider
@ApplicationScoped
public class IllegalArgumentExceptionMapper implements ExceptionMapper<IllegalArgumentException>, HandleExceptionMapper {

    @Context
    HttpServerRequest request;

    @SneakyThrows
    @Override
    public Response toResponse(IllegalArgumentException exception) {
        ErrorResponse errorResponse = buildResponse(exception, request);
        return Response.status(errorResponse.getStatus()).entity(errorResponse).build();
    }

    @Override
    public String getTitle() {
        return HttpResponseStatus.BAD_REQUEST.reasonPhrase();
    }

    @Override
    public int getStatus() {
        return HttpResponseStatus.BAD_REQUEST.code();
    }

}
