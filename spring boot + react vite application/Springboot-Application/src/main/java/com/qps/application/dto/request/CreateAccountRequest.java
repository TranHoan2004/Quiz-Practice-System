package com.qps.application.dto.request;

public record CreateAccountRequest(String name, String email,
                                   String role, String phoneNumber) {
}
