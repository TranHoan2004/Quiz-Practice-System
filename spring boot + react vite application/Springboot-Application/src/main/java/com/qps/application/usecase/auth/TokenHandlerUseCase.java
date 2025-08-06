package com.qps.application.usecase.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.qps.infrastructure.service.jwt.JwtService;
import com.qps.infrastructure.service.jwt.TokenStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.InvalidObjectException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenHandlerUseCase {
    private final JwtService jwtService;

    public Map<String, Object> getToken(String email) throws JOSEException {
        Map<String, Object> map = new HashMap<>();
        map.put("accessToken", jwtService.createAccessToken(email));
        map.put("refreshToken", jwtService.createRefreshToken(email));
        map.put("expiration", jwtService.getExpirationMs());
        map.put("refreshExpiration", jwtService.getExpirationMs() * 7 * 24 * 60 * 60 * 1000);
        return map;
    }

    public Map<String, String> getTokenByRefreshToken(String refreshToken) throws InvalidObjectException, ParseException, JOSEException {
        checkBlacklisted(refreshToken);

        var claims = getClaimsSet(refreshToken);

        var email = claims.getSubject();
        var accessToken = jwtService.createAccessToken(email);
        var newRefreshToken = jwtService.createRefreshToken(email);

        TokenStoreService.blacklistToken(refreshToken);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", newRefreshToken
        );
    }

    public void removeToken(String refreshToken) throws InvalidObjectException, ParseException, JOSEException {
        checkBlacklisted(refreshToken);

        // validate token
        getClaimsSet(refreshToken);

        TokenStoreService.blacklistToken(refreshToken);
    }

    private void checkBlacklisted(String refreshToken) throws InvalidObjectException {
        if (TokenStoreService.isTokenBlacklisted(refreshToken)) {
            throw new InvalidObjectException("Refresh token is invalid");
        }
    }

    private JWTClaimsSet getClaimsSet(String refreshToken) throws InvalidObjectException, ParseException, JOSEException {
        var claims = jwtService.validateToken(refreshToken);
        if (!"refresh".equals(claims.getStringClaim("typ"))) {
            throw new InvalidObjectException("Invalid token type");
        }
        return claims;
    }
}
