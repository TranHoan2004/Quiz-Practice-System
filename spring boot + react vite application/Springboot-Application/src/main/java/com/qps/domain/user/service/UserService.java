package com.qps.domain.user.service;

import com.qps.domain.user.model.Account;

public interface UserService {
    Account getAccountByEmail(String email);
}
