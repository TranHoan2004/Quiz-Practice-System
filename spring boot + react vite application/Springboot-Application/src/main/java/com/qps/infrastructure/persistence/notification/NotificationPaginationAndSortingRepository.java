package com.qps.infrastructure.persistence.notification;

import com.qps.domain.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationPaginationAndSortingRepository extends PagingAndSortingRepository<Notification, String> {
    Page<Notification> findAllByAccountId(String accountId, Pageable pageable);
}
