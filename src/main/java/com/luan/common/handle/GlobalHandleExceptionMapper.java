package com.luan.common.handle;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalHandleExceptionMapper extends HandleExceptionMapper<Exception>
        implements ExceptionMapper<Exception> {

    public String getTitle() {
        return HttpResponseStatus.INTERNAL_SERVER_ERROR.reasonPhrase();
    }

    public int getStatus() {
        return HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
    }

}
