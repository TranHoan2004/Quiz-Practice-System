package com.qps.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "NotificationRequest", description = "Request payload to mark a notification as read")
public record NotificationRequest(
        @Schema(description = "The ID of the notification to mark as read", example = "9b3b6191-7c1a-11f0-81d8-088fc33f56c7")
        String notificationId,

        @Schema(description = "Access token of the user", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken) {
}
