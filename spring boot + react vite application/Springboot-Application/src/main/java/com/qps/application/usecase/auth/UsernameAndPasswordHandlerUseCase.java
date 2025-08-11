package com.qps.application.usecase.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UsernameAndPasswordHandlerUseCase {
    public UserDetails getUserDetailsFromRequest(String username, String password, AuthenticationManager authentication) {
        var manager = authentication.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        log.info("Information: {}", manager.getPrincipal().toString());
        return (UserDetails) manager.getPrincipal();
    }
}
