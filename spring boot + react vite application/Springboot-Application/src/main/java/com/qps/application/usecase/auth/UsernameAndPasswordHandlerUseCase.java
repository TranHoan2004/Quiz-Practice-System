package com.qps.application.usecase.auth;

import com.qps.domain.user.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UsernameAndPasswordHandlerUseCase {
    public UserDetails getUserDetailsFromRequest(String username, String password, AuthenticationManager authentication) {
        log.info("UsernameAndPasswordHandlerUseCase username={} password={}", username, password);
        var manager = authentication.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        log.info("Information: {}", manager.getPrincipal().toString());
        return (UserDetails) manager.getPrincipal();
    }
}
