package com.qps.adapter.rest.notification;

import com.nimbusds.jose.JOSEException;
import com.qps.application.dto.request.NotificationRequest;
import com.qps.application.dto.response.WrapperApiResponse;
import com.qps.application.usecase.notification.NotificationReadEvent;
import com.qps.application.dto.request.TokenRequest;
import com.qps.application.usecase.auth.TokenHandlerUseCase;
import com.qps.application.usecase.notification.NotificationUseCaseHandler;
import com.qps.domain.user.model.Account;
import com.qps.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.ParseException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Tag(
        name = "Notification Controller",
        description = "Handles user notifications, including creation, retrieval, and management of user notifications. Provides endpoints for users to manage their notification settings"
)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {
    TokenHandlerUseCase tokenHandlerUseCase;
    UserService uSrv;
    NotificationUseCaseHandler notificationUseCaseHandler;
    ApplicationEventPublisher eventPublisher;

    @PostMapping("/")
    @Operation(
            summary = "Get all notifications of the current user",
            description = "Accepts an access token, extracts the user's email, and returns a paginated list of notifications belonging to that user.",
            parameters = @Parameter(name = "page", description = "Page index for pagination. Default is 0 (means do not need to handle)."),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User's access token (JWT)",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = TokenRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Notifications fetched successfully",
                            content = @Content(
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "currentPage": 0,
                                                                "totalPages": 2,
                                                                "totalItems": 15,
                                                                "notifications": [
                                                                     {
                                                                          "id": "9b3b6191-7c1a-11f0-81d8-088fc33f56c7",
                                                                          "link": "https://example.com/21",
                                                                          "message": "Message 21",
                                                                          "title": "Title 21",
                                                                          "status": true,
                                                                          "createdDate": "2025-08-18T17:03:36"
                                                                     }
                                                                ]
                                                            }
                                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid token or request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "status": 400,
                                                                "message": "Error happens when encoding token: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:44:06.6938447"
                                                            }
                                                            Or
                                                            {
                                                                "status": 400,
                                                                "message": "Token parsing error: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:45:34.5341059"
                                                            }
                                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "406",
                            description = "Invalid object",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "status": 406,
                                                                "message": "Invalid object: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:46:57.5261482"
                                                            }
                                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Username not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "status": 404,
                                                                "message": "Username not found: org.springframework.security.core.userdetails.UsernameNotFoundException: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:53:50.9674283"
                                                            }
                                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "501",
                            description = "Not implemented: operation not supported",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "status": 501,
                                                                "message": "Operation is not supported: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:42:30.949409"
                                                            }
                                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "status": 500,
                                                                "message": "Error happens in the server: Not implemented",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:39:56.3586874"
                                                            }
                                                            """
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> getAllNotificationsOfCurrentUser(
            @RequestBody TokenRequest req,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) throws InvalidObjectException, ParseException, JOSEException {
        var user = getAccountFromToken(req.token());

        return ResponseEntity.ok(notificationUseCaseHandler.getAllNotificationsByCurrentUser(user.getId(), page));
    }

    @Operation(
            summary = "Mark notification as read",
            description = "Marks a notification as read for the authenticated user and publishes a NotificationReadEvent",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request payload containing the notification ID and user's access token",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = NotificationRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Notification marked as read",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = WrapperApiResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid token or request",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "status": 400,
                                                                "message": "Error happens when encoding token: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:44:06.6938447"
                                                            }
                                                            Or
                                                            {
                                                                "status": 400,
                                                                "message": "Token parsing error: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:45:34.5341059"
                                                            }
                                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "406",
                            description = "Invalid object",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "status": 406,
                                                                "message": "Invalid object: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:46:57.5261482"
                                                            }
                                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Username not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "status": 404,
                                                                "message": "Username not found: org.springframework.security.core.userdetails.UsernameNotFoundException: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:53:50.9674283"
                                                            }
                                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "501",
                            description = "Not implemented: operation not supported",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "status": 501,
                                                                "message": "Operation is not supported: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:42:30.949409"
                                                            }
                                                            """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example =
                                                    """
                                                            {
                                                                "status": 500,
                                                                "message": "Error happens in the server: Not implemented",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:39:56.3586874"
                                                            }
                                                            """
                                    )
                            )
                    )
            }
    )
    @PostMapping("/read")
    public void markNotificationAsRead(@RequestBody NotificationRequest req) throws IOException, ParseException, JOSEException {
        var user = getAccountFromToken(req.accessToken());
        var accountId = user.getId();
        log.info("Marking notification as read for userId: {}, notificationId: {}", accountId, req.notificationId());
        eventPublisher.publishEvent(new NotificationReadEvent(req.notificationId(), accountId));
    }

    private Account getAccountFromToken(String accessToken) throws InvalidObjectException, ParseException, JOSEException {
        var email = tokenHandlerUseCase.getEmailFromAccessToken(accessToken);
        return uSrv.getAccountByEmail(email);
    }
}
