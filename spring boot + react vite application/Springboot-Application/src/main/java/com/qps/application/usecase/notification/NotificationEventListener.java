package com.qps.application.usecase.notification;

import com.qps.domain.notification.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationEventListener {
    NotificationService service;

    @Async
    @EventListener
    public void handleNotificationReadEvent(NotificationReadEvent event) {
        log.info("Handling NotificationReadEvent for notificationId: {}, accountId: {}",
                event.notificationId(), event.accountId());
        service.updateNotificationStatus(event.notificationId(), event.accountId());
    }
}