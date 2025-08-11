package com.qps.domain.user.service;

import com.qps.domain.user.model.Account;
import com.qps.infrastructure.persistence.account.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    AccountRepository repo;

    @InjectMocks
    UserServiceImpl userService;

    String[] NOT_EXISTING_EMAILS = {
            "a@gmail.com", "b@gmail.com",
            "c@gmail.com", "d@gmail.com"
    };

    String[] EXISTING_EMAILS = {
            "admin@gmail.com", "expert@gmail.com",
            "sale@gmail.com", "user@gmail.com"
    };

    @Test
    void getAccountByEmail() {
        System.out.println("---------------- Solve not existing emails case -------------------");
        for (var s : NOT_EXISTING_EMAILS) {
            when(repo.findByEmail(s)).thenReturn(Optional.empty());

            var exception = assertThrows(UsernameNotFoundException.class, () ->
                    userService.getAccountByEmail(s)
            );

            assertNotNull(exception);

            verify(repo, times(1)).findByEmail(s);
            reset(repo);
        }

        System.out.println("---------------- Solve existing emails case -------------------");
        for (var s : EXISTING_EMAILS) {
            var mockAccount = Account.builder().email(s).build();
            when(repo.findByEmail(s)).thenReturn(Optional.of(mockAccount));

            var account = userService.getAccountByEmail(s);

            assertNotNull(account);
            assertEquals(s, account.getEmail());

            verify(repo, times(1)).findByEmail(s);
            reset(repo);
        }
    }
}