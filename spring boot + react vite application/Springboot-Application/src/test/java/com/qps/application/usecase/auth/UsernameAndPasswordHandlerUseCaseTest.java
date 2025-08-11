package com.qps.application.usecase.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsernameAndPasswordHandlerUseCaseTest {
    @Mock
    AuthenticationManager authenticationManager;

    UsernameAndPasswordHandlerUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new UsernameAndPasswordHandlerUseCase();
    }

    @Test
    void getUserDetailsFromRequest() {
        var username = "username1";
        var password = "password1";

        var mockedUserDetails = mock(UserDetails.class);
        var mockedAuthentication = mock(Authentication.class);

        when(mockedAuthentication.getPrincipal()).thenReturn(mockedUserDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockedAuthentication);

        var result = useCase.getUserDetailsFromRequest(username, password, authenticationManager);

        assertNotNull(result);
        assertEquals(mockedUserDetails, result);

        verify(authenticationManager).authenticate(argThat(token ->
                username.equals(token.getPrincipal()) && password.equals(token.getCredentials())
        ));
    }
}