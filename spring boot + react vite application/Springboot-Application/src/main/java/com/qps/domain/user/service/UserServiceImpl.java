package com.qps.domain.user.service;

import com.qps.application.dto.response.AccountResponse;
import com.qps.domain.user.UserException;
import com.qps.domain.user.model.Account;
import com.qps.infrastructure.persistence.account.AccountPaginationAndSortingRepository;
import com.qps.infrastructure.persistence.account.AccountRepository;
import com.qps.infrastructure.persistence.setting.SettingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    SettingRepository sRepo;
    AccountRepository repo;
    AccountPaginationAndSortingRepository psRepo;
    PasswordEncoder encoder;

    @Override
    public Account getAccountByEmail(String email) {
        return repo.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Page<AccountResponse> getUsersListByPageIndex(Pageable pageable) {
        var data = psRepo.findAll(pageable);
        return convertAccountToAccountResponse(data);
    }

    @Override
    public Page<AccountResponse> getUsersByKeyword(Page<Account> prevResult, String keyword, Pageable pageable, boolean signal) {
        log.info("Filter users by keyword: {}", keyword);
        if (keyword != null) {
            // signal true => truong hop role va status khong co noi dung
            if (signal) {
                prevResult = psRepo.findAll(Pageable.unpaged());
            }
            if (!prevResult.getContent().isEmpty()) {
                var filtered = prevResult.getContent()
                        .stream()
                        .filter(account -> {
                            System.out.println("Fullname: " + account.getFullName().toLowerCase() + ", keyword: " + keyword.toLowerCase());
                            return account.getFullName().toLowerCase().contains(keyword.toLowerCase());
                        })
                        .toList();

                var start = (int) pageable.getOffset();
                var end = Math.min(start + pageable.getPageSize(), filtered.size());
                var pagedList = filtered.subList(start, end);

                prevResult = new PageImpl<>(pagedList, pageable, filtered.size());
            }
        }
        return convertAccountToAccountResponse(prevResult);
    }

    @Override
    public Page<Account> filterUsersByRole(String role, Pageable pageable) {
        log.info("Filter users by role: {}", role);
        Page<Account> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

        if (role != null) {
            var setting = sRepo.findByValueIgnoreCase(role);
            page = psRepo.findAllByRole(setting, pageable);
        }

        return page;
    }

    @Override
    public Page<Account> filterUsersByStatus(Page<Account> prevResult, String status, Pageable pageable, boolean signal) {
        log.info("Filter users by status: {}", status);
        if (status != null) {
            var parseStatus = Boolean.parseBoolean(status);
            if (!prevResult.getContent().isEmpty()) {
                var content = prevResult.getContent().stream()
                        .filter(item -> item.getStatus() == parseStatus)
                        .toList();
                prevResult = new PageImpl<>(content, pageable, content.size());
            } else {
                // signal false => truong hop khong co ban ghi tuong ung voi role can tim
                prevResult = signal ? psRepo.findAllByStatus(parseStatus, pageable) : prevResult;
            }
        }
        return prevResult;
    }

    @Override
    public void createAndSaveNewUser(String name, String email, String role, String phoneNumber) {
        log.info("Create new role: {}", role);
        var setting = sRepo.findByValueIgnoreCase(role);
        var account = Account.builder()
                .id(UUID.randomUUID().toString())
                .email(email)
                .fullName(name)
                .password(encoder.encode("YourPassword123."))
                .createdDate(LocalDate.now())
                .status(true)
                .phone(phoneNumber)
                .role(setting)
                .build();
        repo.save(account);
    }

    @Override
    public boolean isEmailExist(String email) {
        return repo.existsByEmail(email);
    }

    @Override
    public boolean isPhoneNumberExist(String phoneNumber) {
        return repo.existsByPhone(phoneNumber);
    }

    @Override
    public void lockUser(String userId) {
        var user = repo.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (user.isAccountNonLocked()) {
            user.setStatus(false);
            repo.save(user);
        }
        throw new UserException(UserException.ErrorCodes.USER_HAS_BEEN_LOCKED, "User has been locked");
    }

    @Override
    public void updateUserInformation(String id, String... info) {

    }

    private Page<AccountResponse> convertAccountToAccountResponse(Page<Account> page) {
        List<AccountResponse> content = new ArrayList<>();
        if (!page.getContent().isEmpty()) {
            content = page.getContent().stream()
                    .map(account -> AccountResponse.builder()
                            .id(account.getId())
                            .email(account.getEmail())
                            .fullName(account.getFullName())
                            .createdDate(account.getCreatedDate().toString())
                            .status(account.getStatus())
                            .phoneNumber(account.getPhone())
                            .role(account.getRole().getValue())
                            .build())
                    .toList();
        }

        return new PageImpl<>(
                content,
                page.getPageable(),
                page.getTotalElements()
        );
    }
}
