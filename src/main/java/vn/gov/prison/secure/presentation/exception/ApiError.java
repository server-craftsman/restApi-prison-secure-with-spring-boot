package vn.gov.prison.secure.presentation.exception;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Standard API error response
 * Following consistent error response format
 */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<ValidationError> validationErrors) {

    public record ValidationError(
            String field,
            String message,
            Object rejectedValue) {
    }

    public static ApiError of(int status, String error, String message, String path) {
        return new ApiError(
                LocalDateTime.now(),
                status,
                error,
                message,
                path,
                List.of());
    }

    public static ApiError withValidationErrors(int status, String error, String message,
            String path, List<ValidationError> errors) {
        return new ApiError(
                LocalDateTime.now(),
                status,
                error,
                message,
                path,
                errors);
    }
}
