package com.qps.infrastructure.persistence.account;

import com.qps.application.dto.response.AccountResponse;
import com.qps.domain.setting.model.Setting;
import com.qps.domain.user.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountPaginationAndSortingRepository extends PagingAndSortingRepository<Account, String> {
    // AccountDAO.getAllExperts()
    @Query("""
            FROM Account
            WHERE role.id = (
                        SELECT id FROM Setting
                        WHERE value = 'Expert'
            )
            """)
    Page<Account> findAllExperts(Pageable pageable);

    Page<Account> findAllByRole(Setting role, Pageable pageable);

    Page<Account> findAllByStatus(Boolean status, Pageable pageable);

    Page<AccountResponse> findAllByFullNameContainingIgnoreCase(String fullName, Pageable pageable);
}
