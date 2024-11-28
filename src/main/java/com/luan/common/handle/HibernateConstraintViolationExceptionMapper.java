package com.luan.common.handle;

import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;

@Provider
public class HibernateConstraintViolationExceptionMapper extends HandleExceptionMapper<ConstraintViolationException>
        implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public String getTitle() {
        return "Conflito de dados";
    }

    @Override
    public int getStatus() {
        return HttpResponseStatus.BAD_REQUEST.code();
    }

}
