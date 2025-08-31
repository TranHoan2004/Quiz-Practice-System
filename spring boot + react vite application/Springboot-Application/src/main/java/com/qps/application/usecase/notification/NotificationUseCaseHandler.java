package com.qps.application.usecase.notification;

import com.qps.domain.notification.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationUseCaseHandler {
    NotificationService service;

    public Map<String, Object> getAllNotificationsByCurrentUser(String accountId, int page) {
        log.info("Fetching all notifications for account ID: {}", accountId);
        return service.getNotificationsByUser(accountId, page);
    }
}
