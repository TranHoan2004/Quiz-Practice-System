package com.qps.adapter;

import com.nimbusds.jose.JOSEException;
import com.qps.application.dto.response.WrapperApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Operation(
            summary = "Handle RuntimeException",
            description = "Catches RuntimeException and returns HTTP 500 Internal Server Error",
            responses = {
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = WrapperApiResponse.class,
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
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        var msg = "Error happens in the server: " + e.getMessage();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, msg, null);
    }

    @Operation(
            summary = "Handle UnsupportedOperationException",
            description = "Catches UnsupportedOperationException and returns HTTP 501 Not Implemented",
            responses = {
                    @ApiResponse(
                            responseCode = "501",
                            description = "Operation not supported",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = WrapperApiResponse.class,
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
            }
    )
    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    public ResponseEntity<?> handleUnsupportedOperationException(UnsupportedOperationException e) {
        var msg = "Operation is not supported: " + e.getMessage();
        return buildErrorResponse(HttpStatus.NOT_IMPLEMENTED, msg, null);
    }

    @Operation(
            summary = "Handle token-related exceptions",
            description = "Catches JOSEException or ParseException and returns HTTP 400 Bad Request",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "Token encoding or parsing error",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = WrapperApiResponse.class,
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
                    )
            }
    )
    @ExceptionHandler({JOSEException.class, ParseException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleTokenException(Exception e) {
        var message = switch (e) {
            case JOSEException jose -> "Error happens when encoding token: " + jose.getMessage();
            case ParseException parse -> "Token parsing error: " + parse.getMessage();
            default -> "Unknown error related to token.";
        };
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, null);
    }

    @Operation(
            summary = "Handle InvalidObjectException",
            description = "Catches InvalidObjectException and returns HTTP 406 Not Acceptable",
            responses = {
                    @ApiResponse(
                            responseCode = "406",
                            description = "Invalid object",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = WrapperApiResponse.class,
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
                    )
            }
    )
    @ExceptionHandler(InvalidObjectException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<?> handleInvalidObjectException(InvalidObjectException e) {
        var message = "Invalid object: " + e.getMessage();
        return buildErrorResponse(HttpStatus.NOT_ACCEPTABLE, message, null);
    }

    @Operation(
            summary = "Handle validation errors",
            description = "Catches MethodArgumentNotValidException and returns HTTP 400 Bad Request with field error details",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "Validation failed",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = WrapperApiResponse.class,
                                            example =
                                                    """
                                                            {
                                                              "status": 400,
                                                              "message": "string",
                                                              "data": {},
                                                              "timestamp": "2025-08-25T09:34:11.109Z"
                                                            }
                                                            """
                                    )
                            )
                    )
            }
    )
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach((error) ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        Map<String, Map<String, String>> fieldErrors = new HashMap<>();
        fieldErrors.put("errors", errors);

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", fieldErrors);
    }

    @Operation(
            summary = "Handle IOException",
            description = "Catches IOException and returns HTTP 400 Bad Request",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "IO error",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = WrapperApiResponse.class,
                                            example =
                                                    """
                                                            {
                                                                "status": 400,
                                                                "message": "Error happens during I/O operations: Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:49:58.1752074"
                                                            }
                                                            """
                                    )
                            )
                    )
            }
    )
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleIOException(IOException e) {
        var message = "Error happens during I/O operations: " + e.getMessage();
        return buildErrorResponse(HttpStatus.BAD_REQUEST, message, null);
    }

    @Operation(
            summary = "Handle IllegalArgumentException",
            description = "Catches IllegalArgumentException and returns HTTP 400 Bad Request",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "Illegal argument",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = WrapperApiResponse.class,
                                            example =
                                                    """
                                                            {
                                                                "status": 400,
                                                                "message": "Error",
                                                                "data": null,
                                                                "timestamp": "2025-08-25T16:52:43.8472315"
                                                            }
                                                            """
                                    )
                            )
                    )
            }
    )
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @Operation(
            summary = "Handle UsernameNotFoundException",
            description = "Catches UsernameNotFoundException and returns HTTP 404 Not Found",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Username not found",
                            content = @Content(
                                    schema = @Schema(
                                            implementation = WrapperApiResponse.class,
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
            }
    )
    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e) {
        var message = "Username not found: " + e;
        return buildErrorResponse(HttpStatus.NOT_FOUND, message, null);
    }

    private ResponseEntity<WrapperApiResponse<?>> buildErrorResponse(
            HttpStatus status, String message, Object data) {
        return ResponseEntity.status(status)
                .body(new WrapperApiResponse<>(
                        status.value(),
                        message,
                        data,
                        LocalDateTime.now()
                ));
    }
}
