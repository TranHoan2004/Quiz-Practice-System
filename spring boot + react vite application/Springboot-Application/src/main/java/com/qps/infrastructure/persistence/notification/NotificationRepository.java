package com.qps.infrastructure.persistence.notification;

import com.qps.domain.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {
    Optional<Notification> findByIdAndAccount_Id(String id, String accountId);
}
