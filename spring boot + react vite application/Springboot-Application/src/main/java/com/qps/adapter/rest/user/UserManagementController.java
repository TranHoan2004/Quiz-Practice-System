package com.qps.adapter.rest.user;

import com.qps.application.dto.request.CreateAccountRequest;
import com.qps.application.dto.response.WrapperApiResponse;
import com.qps.application.usecase.user.CreateUserEvent;
import com.qps.application.usecase.user.UserManagementUseCaseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@SecurityRequirement(name = "bearerToken")
@Tag(
        name = "User Management Controller",
        description = """
                APIs for managing users.
                Provides endpoints to retrieve paginated user lists with optional filters
                (role, status, keyword) and supports secured access via Bearer Token.
                """
)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserManagementController {
    UserManagementUseCaseHandler useCase;
    ApplicationEventPublisher eventPublisher;

    @GetMapping("/all")
    @Operation(
            summary = "Get Users",
            description = "Retrieve a paginated list of users. Supports optional filters by role, status, and search keyword."
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")
            })
    public ResponseEntity<?> getUsers(
            @RequestParam(value = "role", required = false) String role,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "search", required = false) String keyword,
            @RequestParam("page") int page,
            @RequestParam(value = "size", required = false) Integer size) {
        var pageIndex = page - 1;
        log.info("search={}, status={}, page={}, size={}", keyword, status, pageIndex, size);
        return ResponseEntity.ok(keyword == null && role == null && status == null ?
                useCase.getListOfUsers(pageIndex, size) : useCase.filterUsersByRoleOrStatusOrKeyword(role, status, keyword, pageIndex, size));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody CreateAccountRequest request) {
        log.info("request={}", request);
        useCase.createNewUser(request);
        eventPublisher.publishEvent(new CreateUserEvent(request.email(), request.name()));
        return ResponseEntity.ok(WrapperApiResponse.builder()
                .status(200)
                .message("Create user successfully")
                .timestamp(LocalDateTime.now())
                .build());
    }
}
