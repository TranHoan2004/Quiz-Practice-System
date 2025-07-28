package com.qps.domain.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_ADMIN("ADMINISTRATOR"),
    ROLE_USER("USER"),
    ROLE_MARKETING("MARKETING"),
    ROLE_SALER("SALER"),
    ROLE_EXPERT("EXPERT");

    private final String value;

    public static String fromValue(String value) {
        for (Role status : Role.values()) {
            if (status.getValue().equalsIgnoreCase(value)) {
                return status.name();
            }
        }
        return null; // or throw an exception if preferred
    }
}
