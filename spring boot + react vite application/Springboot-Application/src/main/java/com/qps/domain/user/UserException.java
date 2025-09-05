package com.qps.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserException extends RuntimeException {
    @Getter
    ErrorCodes errorCode;

    public UserException(ErrorCodes code, String message) {
        super(message);
        this.errorCode = code;
    }

    public enum ErrorCodes {
        USER_HAS_BEEN_LOCKED,
        EMAIL_EXISTED,
        PHONE_NUMBER_EXISTED
    }
}
