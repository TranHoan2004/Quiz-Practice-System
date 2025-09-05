package com.qps.application.dto.request;

import jakarta.validation.constraints.Size;

public record EditAccountRequest(
        String email,
        String fullName,

        @Size(max = 32, min = 8)
        String password,

        String dob,
        String gender,
        String createdDate,
        Boolean status,

        @Size(max = 11)
        String phone,

        String imageUrl,
        String role) {
}
