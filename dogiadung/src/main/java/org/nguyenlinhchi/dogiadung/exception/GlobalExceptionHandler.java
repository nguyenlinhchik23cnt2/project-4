package org.nguyenlinhchi.dogiadung.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Bắt mọi exception từ tất cả Controller → trả JSON thống nhất.
 * Không cần try/catch trong Controller hay Service.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ── 404 Not Found ──────────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        log.warn("404: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(404, ex.getMessage()));
    }

    // ── 400 Validation Error (@Valid thất bại) ─────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field   = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(field, message);
        });

        Map<String, Object> body = Map.of(
            "timestamp", LocalDateTime.now().toString(),
            "status",    400,
            "message",   "Dữ liệu không hợp lệ",
            "errors",    errors
        );

        log.warn("400 Validation: {}", errors);
        return ResponseEntity.badRequest().body(body);
    }

    // ── 400 Illegal Argument ────────────────────────────────────
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArg(IllegalArgumentException ex) {
        log.warn("400 IllegalArgument: {}", ex.getMessage());
        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse(400, ex.getMessage()));
    }

    // ── 500 Catch-all ──────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        log.error("500 Unexpected error: ", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(500, "Lỗi hệ thống: " + ex.getMessage()));
    }

    // ── Inner class: cấu trúc JSON lỗi ─────────────────────────
    public record ErrorResponse(int status, String message) {
        // Jackson tự serialize thành {"status": 404, "message": "..."}
    }
}
