package com.qps.application.usecase.notification;

import com.qps.domain.notification.model.Notification;
import com.qps.domain.notification.service.NotificationServiceImpl;
import com.qps.infrastructure.persistence.notification.NotificationPaginationAndSortingRepository;
import org.apache.poi.ss.formula.functions.T;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class NotificationUseCaseHandlerTest {
    @Autowired
    NotificationUseCaseHandler handler;

    @ParameterizedTest
    @CsvSource({
            "'b287bbd5-397a-11f0-84a1-088fc33f56c7', 0",
            "'b287bbd5-397a-11f0-84a1-088fc33f56c7', 1",
            "'b287bbd5-397a-11f0-84a1-088fc33f56c7', 2"
    })
    void getAllNotificationsByCurrentUser(String accountId, int page) {
        Map<String, Object> result = handler.getAllNotificationsByCurrentUser(accountId, page);
        System.out.println("=======================================");
        System.out.println(result);

        assertNotNull(result, "Result should not be null");
        @SuppressWarnings("unchecked")
        List<T> notifications = (List<T>) result.get("notifications");
        System.out.println("Size: " + notifications.size());

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
}