package com.warrenbuffett.server.common.exception;

public class LoginFailureException extends RuntimeException {

    public LoginFailureException(){
        super(ErrorCode.LOGIN_FAILED.getMessage());
    }

    private LoginFailureException(String msg){
        super(msg);
    }
}
