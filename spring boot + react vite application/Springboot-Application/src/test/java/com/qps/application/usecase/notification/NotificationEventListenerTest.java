package com.qps.application.usecase.notification;

import com.qps.domain.notification.model.Notification;
import com.qps.domain.notification.service.NotificationService;
import com.qps.infrastructure.persistence.notification.NotificationRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(MockitoExtension.class)
class NotificationEventListenerTest {
    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    NotificationRepository repo;

    @MockBean
    NotificationService service;

    @ParameterizedTest
    @CsvSource({
            "'9b3b6191-7c1a-11f0-81d8-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7'",
            "'9b3b6216-7c1a-11f0-81d8-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7'",
            "'9b3b629e-7c1a-11f0-81d8-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7'"
    })
    void handleNotificationReadEventWithCorrectParameters(String notificationId, String accountId) {
        Optional<Notification> oldNoti = repo.findByIdAndAccount_Id(notificationId, accountId);
        boolean currentStatus = oldNoti.isPresent() && oldNoti.get().getStatus();

        publisher.publishEvent(new NotificationReadEvent(notificationId, accountId));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        verify(service, times(1)).updateNotificationStatus(notificationId, accountId);

        Optional<Notification> noti = repo.findByIdAndAccount_Id(notificationId, accountId);

        assert noti.isPresent();
        assertTrue(noti.get().getStatus());

        noti.get().setStatus(currentStatus);
        repo.save(noti.get());
    }

    @ParameterizedTest
    @CsvSource({
            "'9b3b6192-7c1a-11f0-81d8-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7'", // wrong notification id
            "'9b3b6216-7c1a-11f0-81d8-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c8'", // wrong account id
            "'9b3b630e-7c1a-11f0-81d8-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c8'" // wrong both
    })
    void handleNotificationReadEventWithIncorrectParameters(String notificationId, String accountId) {
        try {
            publisher.publishEvent(new NotificationReadEvent(notificationId, accountId));
            Thread.sleep(5000);
            verify(service, times(1)).updateNotificationStatus(notificationId, accountId);
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}