package com.qps.infrastructure.persistence.account;

import com.qps.domain.user.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    // AccountDAO.getAccountByEmail(String email)
    Optional<Account> findByEmail(String username);

    // AccountDAO.isEmailExist(String email)
    boolean existsByEmail(String email);

    // AccountDAO.isPhoneNumberExist(String phoneNumber)
    boolean existsByPhone(String phone);

    // AccountDAO.getCountNewAccountByDate(String startDate, String endDate)
    int countByCreatedDateBetween(LocalDate createdDateAfter, LocalDate createdDateBefore);
}
