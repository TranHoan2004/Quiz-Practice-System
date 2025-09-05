package com.qps.adapter.rest.user;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.oauth2.sdk.TokenResponse;
import com.qps.application.dto.request.LoginRequest;
import com.qps.application.dto.request.TokenRequest;
import com.qps.application.dto.response.WrapperApiResponse;
import com.qps.application.usecase.auth.ProfileUtil;
import com.qps.application.usecase.auth.TokenHandlerUseCase;
import com.qps.application.usecase.auth.UsernameAndPasswordHandlerUseCase;
import com.qps.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.io.InvalidObjectException;
import java.text.ParseException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(
        name = "Authentication API",
        description = "Endpoints for user authentication, including login and token generation. Handles credential verification and issues access/refresh tokens for secure access."
)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
    TokenHandlerUseCase tokenHandlerUseCase;
    UsernameAndPasswordHandlerUseCase upUseCase;
    AuthenticationManager authenticationManager;
    UserService uSrv;

    @Operation(
            summary = "Login with username and password",
            description = "Authenticate user using email and password. Returns access and refresh tokens if authentication is successful."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful, tokens returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request: malformed input or JOSE/Parse errors",
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
                    responseCode = "401",
                    description = "Authentication failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = WrapperApiResponse.class)
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
                    responseCode = "406",
                    description = "Not acceptable: invalid object",
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
            )
    })
    @PostMapping(value = "/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> login(
            @RequestBody
            @Parameter(description = "Login request containing email and password", required = true)
            @Valid
            LoginRequest request) throws JOSEException {
        // Get user information, who has that email and password
        var userDetails = upUseCase.getUserDetailsFromRequest(request.email(), request.password(), authenticationManager);
        var profile = uSrv.getAccountByEmail(userDetails.getUsername());

        // Get access data and refresh data from this email
        Map<String, Object> data = tokenHandlerUseCase.getTokenFromEmail(userDetails.getUsername());
        ProfileUtil.getProfile(profile, data);

        return ResponseEntity.ok(data);
    }

    /*
     * React chuyen huong ve security cua spring: localhost:8000/oauth2/authorization/google
     * Spring security chuyen huong den Google Authorization Endpoint (https://accounts.google.com/o/oauth2/v2/auth?client_id=...&redirect_uri=...&response_type=code&scope=openid email profile)
     * Google chuyen huong ve endpoint da dang ky (/oauth2/google/login) kem code
     * Spring boot tu dong goi token-uri de lay access token, goi user-info-uri de lay thong tin nguoi dung
     */

    @Operation(
            summary = "Refresh access token",
            description = "Receive a refresh token and return a new access token if valid."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request: malformed input, JOSEException, ParseException, validation errors, or I/O errors",
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
                    description = "Not acceptable: invalid object",
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
            )
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRequest req) throws InvalidObjectException, ParseException, JOSEException {
        return ResponseEntity.ok(tokenHandlerUseCase.getTokenByRefreshToken(req.token()));
    }
}
