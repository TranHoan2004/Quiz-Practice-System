package com.qps.domain.notification.service;

import com.qps.application.usecase.notification.NotificationReadEvent;
import com.qps.domain.notification.model.Notification;
import com.qps.infrastructure.persistence.notification.NotificationPaginationAndSortingRepository;
import com.qps.infrastructure.persistence.notification.NotificationRepository;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {
    @Autowired
    NotificationService service;

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    NotificationRepository repo;

    @ParameterizedTest
    @CsvSource({
            "'b287bbd5-397a-11f0-84a1-088fc33f56c7', 0",
            "'b287bbd5-397a-11f0-84a1-088fc33f56c7', 1",
            "'b287bbd5-397a-11f0-84a1-088fc33f56c7', 2"
    })
    void getNotificationsByUser(String accountId, int page) {
        System.out.println(accountId + ", " + page);
        Map<String, Object> result = service.getNotificationsByUser(accountId, page);

        assertNotNull(result, "Result should not be null");
        System.out.println(result);
        @SuppressWarnings("unchecked")
        List<T> notifications = (List<T>) result.get("notifications");

        if (page == 2) {
            assertTrue(notifications.isEmpty(), "Should be empty");
        } else {
            assertFalse(notifications.isEmpty(), "Should not be empty");
        }

        assertEquals(page, result.get("currentPage"), "Wrong page number");

        assertNotNull(result.get("totalItems"), "Total items should not be null");
        assertNotNull(result.get("totalPages"), "Total pages should not be null");

        assertInstanceOf(Long.class, result.get("totalItems"), "Total items should be a string");
        assertInstanceOf(Integer.class, result.get("totalPages"), "Total pages should be a string");
    }

    @ParameterizedTest
    @CsvSource({
            "'9b3b6191-7c1a-11f0-81d8-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7'",
            "'9b3b6216-7c1a-11f0-81d8-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7'",
            "'9b3b629e-7c1a-11f0-81d8-088fc33f56c7', 'b287bbd5-397a-11f0-84a1-088fc33f56c7'"
    })
    void updateNotificationStatusWithCorrectParameters(String notificationId, String accountId) {
        System.out.println("Notification id: " + notificationId + ", account id: " + accountId);
        Optional<Notification> oldNoti = repo.findByIdAndAccount_Id(notificationId, accountId);
        System.out.println("Old notification: " + oldNoti);
        boolean currentStatus = oldNoti.isPresent() && oldNoti.get().getStatus();

        publisher.publishEvent(new NotificationReadEvent(notificationId, accountId));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

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
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}