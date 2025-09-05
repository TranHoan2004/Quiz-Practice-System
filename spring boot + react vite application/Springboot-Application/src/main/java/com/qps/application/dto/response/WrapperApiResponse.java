package com.qps.application.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WrapperApiResponse<T>(
        int status,
        String message,
        T data,
        LocalDateTime timestamp
) {
}
