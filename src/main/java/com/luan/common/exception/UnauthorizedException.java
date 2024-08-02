package com.luan.common.exception;

public class UnauthorizedException extends AuthenticationException {

    public UnauthorizedException(String message) {
        super(message);
    }

}
