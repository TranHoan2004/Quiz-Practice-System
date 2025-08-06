package com.qps.application.dto.response;

import java.time.LocalDateTime;

public record WrapperApiResponse<T>(
        int status,
        String message,
        T data,
        LocalDateTime timestamp
) {
}
