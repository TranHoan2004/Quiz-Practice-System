package com.qps.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User's access token (JWT)")
public record TokenRequest(
        String token
) {
}
