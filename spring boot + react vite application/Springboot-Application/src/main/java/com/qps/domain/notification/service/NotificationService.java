package com.qps.domain.notification.service;

import java.util.Map;

public interface NotificationService {
    Map<String, Object> getNotificationsByUser(String userId, int page);

    void updateNotificationStatus(String notificationId, String accountId);
}
