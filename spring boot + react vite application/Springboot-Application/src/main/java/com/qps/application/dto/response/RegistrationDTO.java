package com.qps.application.dto.response;

public record RegistrationDTO(
        String id, String email, String registrationTime,
        String subject, String packageName, String totalCost,
        String status, String validFrom, String validTo
) {
}
