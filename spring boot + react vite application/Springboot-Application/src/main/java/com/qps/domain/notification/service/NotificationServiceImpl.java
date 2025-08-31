package com.qps.domain.notification.service;

import com.qps.domain.notification.model.Notification;
import com.qps.infrastructure.persistence.notification.NotificationPaginationAndSortingRepository;
import com.qps.infrastructure.persistence.notification.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationServiceImpl implements NotificationService {
    NotificationPaginationAndSortingRepository psRepo;
    NotificationRepository repo;
    final Integer NUMBER_OF_RECORDS_PER_PAGE = 10;

    @Override
    public Map<String, Object> getNotificationsByUser(String userId, int page) {
        var pageable = PageRequest.of(
                page,
                NUMBER_OF_RECORDS_PER_PAGE,
                Sort.by(Sort.Direction.ASC, "createdDate")
        );
        Page<Notification> data = psRepo.findAllByAccountId(userId, pageable);
        return Map.of(
                "notifications", data.getContent(),
                "currentPage", data.getNumber(),
                "totalItems", data.getTotalElements(),
                "totalPages", data.getTotalPages()
        );
    }

    @Override
    public void updateNotificationStatus(String notificationId, String accountId) {
        Notification notification = repo.findByIdAndAccount_Id(notificationId, accountId)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        notification.markAsRead();
        log.info("Notification updated successfully: {}", notification.getStatus());
        repo.save(notification);
    }
}
