package com.luan.common.handle;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;

@Provider
public class IllegalStateExceptionMapper extends HandleExceptionMapper<IllegalStateException>
        implements ExceptionMapper<IllegalStateException> {

    @Override
    public String getTitle() {
        return "Operação inválida";
    }

    @Override
    public int getStatus() {
        return HttpResponseStatus.BAD_REQUEST.code();
    }

}
