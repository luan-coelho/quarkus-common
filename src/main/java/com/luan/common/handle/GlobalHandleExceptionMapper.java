package com.luan.common.handle;

import com.luan.common.handle.rest.response.ErrorResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpServerRequest;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import lombok.SneakyThrows;

@Provider
public class GlobalHandleExceptionMapper implements ExceptionMapper<Exception>, HandleExceptionMapper {

    @Context
    HttpServerRequest request;

    @SneakyThrows
    @Override
    public Response toResponse(Exception exception) {
        ErrorResponse errorResponse = buildResponse(exception, request);
        return Response.status(errorResponse.getStatus()).entity(errorResponse).build();
    }


    public String getTitle() {
        return HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase();
    }

    public int getStatus() {
        return HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
    }

}
