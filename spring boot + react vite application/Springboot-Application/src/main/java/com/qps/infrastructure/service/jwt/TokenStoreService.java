package com.qps.infrastructure.service.jwt;

import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public class TokenStoreService {
    private static final Set<String> blacklistedTokens = new HashSet<>();

    public static void blacklistToken(String token) {
        log.info("Add token to blacklist: {}", token);
        blacklistedTokens.add(token);
    }

    public static boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
