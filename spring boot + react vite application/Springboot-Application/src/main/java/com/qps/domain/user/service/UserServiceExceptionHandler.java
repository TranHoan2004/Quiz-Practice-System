package com.qps.domain.user.service;

public class UserServiceExceptionHandler extends RuntimeException {
    public UserServiceExceptionHandler(String message) {
        super(message);
    }
}
