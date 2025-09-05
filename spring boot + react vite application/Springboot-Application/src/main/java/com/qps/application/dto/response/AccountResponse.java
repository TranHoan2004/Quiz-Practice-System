package com.qps.application.dto.response;

import lombok.Builder;

@Builder
public record AccountResponse(
        String id,
        String email,
        String fullName,
        String dob,
        String gender,
        String createdDate,
        Boolean status,
        String phoneNumber,
        String avatarUrl,
        String role) {
}
