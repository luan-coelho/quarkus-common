package com.luan.common.handle;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalHandleExceptionMapper extends HandleExceptionMapper<Exception>
        implements ExceptionMapper<Exception> {

    public String getTitle() {
        return "Erro Interno do Servidor";
    }

    public int getStatus() {
        return HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
    }

}
