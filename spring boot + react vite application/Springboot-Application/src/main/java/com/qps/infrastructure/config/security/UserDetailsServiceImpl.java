package com.qps.infrastructure.config.security;

import com.qps.domain.user.model.Account;
import com.qps.infrastructure.persistence.account.AccountRepository;
import com.qps.infrastructure.persistence.setting.SettingForAccountRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    AccountRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername");
        Account acc = repo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
        log.info("User details: {}", acc);
        return new User(acc.getEmail(), acc.getPassword(), acc.getAuthorities());
    }
}
