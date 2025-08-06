package com.qps.adapter.rest.user;

import com.nimbusds.jose.JOSEException;
import com.qps.application.dto.response.WrapperApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AuthExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
        var msg = "Error happens in the server: " + e.getMessage();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, msg, null);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<?> handleUnsupportedOperationException(UnsupportedOperationException e) {
        var msg = "Operation is not supported: " + e.getMessage();
        return buildErrorResponse(HttpStatus.NOT_IMPLEMENTED, msg, null);
    }

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

    @ExceptionHandler(InvalidObjectException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<?> handleInvalidObjectException(InvalidObjectException e) {
        return buildErrorResponse(HttpStatus.NOT_ACCEPTABLE, e.getMessage(), null);
    }

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

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleIOException(IOException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleIllegalArgumentException(IllegalArgumentException e) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
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
