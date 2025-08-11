package com.qps.application.usecase.auth;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.qps.infrastructure.service.jwt.JwtService;
import com.qps.infrastructure.service.jwt.TokenStoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InvalidObjectException;
import java.text.ParseException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenHandlerUseCaseTest {
    String sampleEmail = "sample@gmail.com";

    @Mock
    JwtService jwtService;

    @InjectMocks
    TokenHandlerUseCase tokenHandlerUseCase;

    @ParameterizedTest
    @ValueSource(strings = {
            "a@gmail.com", "b@gmail.com"
    })
    void getTokenFromEmail(String input) {
        try {
            when(jwtService.createAccessToken(input)).thenReturn("mocked-access-token");
            when(jwtService.createRefreshToken(input)).thenReturn("mocked-refresh-token");
            when(jwtService.getExpirationMs()).thenReturn(604800000L); // 7 days

            Map<String, Object> map = tokenHandlerUseCase.getTokenFromEmail(input);

            assertNotNull(map);
            System.out.println(map);

            assertTrue(map.containsKey("accessToken"));
            assertTrue(map.containsKey("refreshToken"));
            assertTrue(map.containsKey("expiration"));
            assertTrue(map.containsKey("refreshExpiration"));

            assertNotNull(map.get("accessToken"));
            assertNotNull(map.get("refreshToken"));
            assertNotNull(map.get("expiration"));
            assertNotNull(map.get("refreshExpiration"));

            var expiration = (Long) map.get("expiration");
            var refreshExpiration = (Long) map.get("refreshExpiration");

            assertEquals(expiration, refreshExpiration / (7 * 24 * 60 * 60 * 1000));
        } catch (JOSEException e) {
            assertNull(e);
        }
    }

    @Test
    void getTokenByRefreshToken() {
        var refreshToken = "valid-refresh-token";

        // Case 1: Token is not blacklisted
        System.out.println("Case 1: Token is not blacklisted");
        try (var tokenStoreMock = mockStatic(TokenStoreService.class)) {
            tokenStoreMock.when(() -> TokenStoreService.isTokenBlacklisted(refreshToken)).thenReturn(false);
            tokenStoreMock.when(() -> TokenStoreService.blacklistToken(refreshToken)).thenAnswer(invocation -> null);

            var claims = new JWTClaimsSet.Builder()
                    .subject(sampleEmail)
                    .claim("type", "refresh")
                    .build();

            when(jwtService.validateToken(refreshToken)).thenReturn(claims);
            when(jwtService.createAccessToken(sampleEmail)).thenReturn("new-access-token");
            when(jwtService.createRefreshToken(sampleEmail)).thenReturn("new-refresh-token");
            when(jwtService.getExpirationMs()).thenReturn(604800000L);

            Map<String, Object> result = tokenHandlerUseCase.getTokenByRefreshToken(refreshToken);

            assertNotNull(result);
            System.out.println(result);

            assertEquals("new-access-token", result.get("accessToken"));
            assertEquals("new-refresh-token", result.get("refreshToken"));
            assertEquals(604800000L, result.get("expiration"));
            assertEquals(604800000L * 7 * 24 * 60 * 60 * 1000, result.get("refreshExpiration"));

            tokenStoreMock.verify(() -> TokenStoreService.isTokenBlacklisted(refreshToken), times(1));
            tokenStoreMock.verify(() -> TokenStoreService.blacklistToken(refreshToken), times(1));
        } catch (InvalidObjectException | ParseException | JOSEException e) {
            assertNotNull(e);
        }

        // Case 2: Token is blacklisted
        System.out.println("Case 2: Token is blacklisted");
        try (var tokenStoreMock = mockStatic(TokenStoreService.class)) {
            tokenStoreMock.when(() -> TokenStoreService.isTokenBlacklisted(refreshToken)).thenReturn(true);

            InvalidObjectException exception = assertThrows(InvalidObjectException.class, () -> {
                tokenHandlerUseCase.getTokenByRefreshToken(refreshToken);
            });

            assertNotNull(exception);
            System.out.println(exception.getMessage());

            tokenStoreMock.verify(() -> TokenStoreService.isTokenBlacklisted(refreshToken), times(1));
            tokenStoreMock.verify(() -> TokenStoreService.blacklistToken(refreshToken), never());
        }
    }

    @Test
    void removeToken() {
    }
}