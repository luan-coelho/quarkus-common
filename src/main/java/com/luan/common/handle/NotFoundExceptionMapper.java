package com.luan.common.handle;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper extends HandleExceptionMapper<NotFoundException>
        implements ExceptionMapper<NotFoundException> {

    @Override
    public String getTitle() {
        return HttpResponseStatus.NOT_FOUND.reasonPhrase();
    }

    @Override
    public int getStatus() {
        return HttpResponseStatus.NOT_FOUND.code();
    }

}
