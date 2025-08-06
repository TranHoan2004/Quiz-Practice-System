package com.qps.domain.user.service;

import com.qps.domain.user.model.Account;
import com.qps.infrastructure.persistence.account.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    AccountRepository repo;

    @Override
    public Account getAccountByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
