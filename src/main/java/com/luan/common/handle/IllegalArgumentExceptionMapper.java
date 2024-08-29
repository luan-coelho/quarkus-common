package com.luan.common.handle;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
@ApplicationScoped
public class IllegalArgumentExceptionMapper extends HandleExceptionMapper<IllegalArgumentException> implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public String getTitle() {
        return HttpResponseStatus.BAD_REQUEST.reasonPhrase();
    }

    @Override
    public int getStatus() {
        return HttpResponseStatus.BAD_REQUEST.code();
    }

}
