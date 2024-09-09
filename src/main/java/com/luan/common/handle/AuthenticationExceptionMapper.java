package com.luan.common.handle;

import com.luan.common.exception.AuthenticationException;
import io.netty.handler.codec.http.HttpResponseStatus;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AuthenticationExceptionMapper extends HandleExceptionMapper<AuthenticationException>
        implements ExceptionMapper<AuthenticationException> {

    @Override
    public String getTitle() {
        return HttpResponseStatus.UNAUTHORIZED.reasonPhrase();
    }

    @Override
    public int getStatus() {
        return HttpResponseStatus.UNAUTHORIZED.code();
    }

}
