package com.qps.infrastructure.config.security;

import lombok.Getter;

@Getter()
public enum Role {
    USER,
    ADMIN,
    SALES,
    MARKETING,
    EXPERT;

    public static String[] getAuthorities() {
        return new String[]{USER.name(), ADMIN.name(), SALES.name(), MARKETING.name(), EXPERT.name()};
    }
}
