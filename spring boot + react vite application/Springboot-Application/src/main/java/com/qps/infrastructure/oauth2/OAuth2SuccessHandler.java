package com.qps.infrastructure.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.qps.application.usecase.auth.ProfileUtil;
import com.qps.domain.user.model.Account;
import com.qps.domain.user.service.UserService;
import com.qps.infrastructure.service.jwt.JwtService;
import com.qps.infrastructure.utils.EncodeUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    JwtService jwtService;
    ObjectMapper objectMapper;
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        var user = (OAuth2User) authentication.getPrincipal();

        log.info("user={}", user);
        String email = user.getAttribute("email");
        var account = userService.getAccountByEmail(email);
        Map<String, Object> responseMap = new HashMap<>();
        try {
            responseMap.put("accessToken", jwtService.createAccessToken(email));
            responseMap.put("refreshToken", jwtService.createRefreshToken(email));
            responseMap.put("expiration", jwtService.getExpirationMs());
            responseMap.put("refreshExpiration", jwtService.getExpirationMs() * 7 * 24 * 60 * 60 * 1000);
        } catch (JOSEException e) {
            log.error(e.getMessage());
        }
        ProfileUtil.getProfile(account, responseMap);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        var json = objectMapper.writeValueAsString(responseMap);
        var encoded = EncodeUtil.encode(json);

        var redirectUrl = UriComponentsBuilder
                .fromUriString("http://localhost:5173/signin")
                .queryParam("data", encoded)
                .build().toUriString();

        response.sendRedirect(redirectUrl);
    }
}
