package com.qps.domain.user.service;

import com.qps.application.dto.response.AccountResponse;
import com.qps.domain.user.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Account getAccountByEmail(String email);

    Page<AccountResponse> getUsersListByPageIndex(Pageable pageable);

    Page<AccountResponse> getUsersByKeyword(Page<Account> prevResult, String keyword, Pageable pageable, boolean signal);

    Page<Account> filterUsersByRole(String role, Pageable pageable);

    Page<Account> filterUsersByStatus(Page<Account> prevResult, String status, Pageable pageable, boolean signal);

    void createAndSaveNewUser(String name, String email, String role, String phoneNumber);

    boolean isEmailExist(String email);

    boolean isPhoneNumberExist(String phoneNumber);

    void lockUser(String userId);

    void updateUserInformation(String id, String... info);
}
