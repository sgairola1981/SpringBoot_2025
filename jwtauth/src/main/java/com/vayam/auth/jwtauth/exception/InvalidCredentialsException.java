package com.vayam.auth.jwtauth.exception;

public class InvalidCredentialsException  extends RuntimeException {
    public InvalidCredentialsException (String message) {
        super(message);
    }
}
