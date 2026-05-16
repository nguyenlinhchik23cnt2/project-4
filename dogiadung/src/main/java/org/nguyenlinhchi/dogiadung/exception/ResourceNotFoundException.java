package org.nguyenlinhchi.dogiadung.exception;

/**
 * Ném khi không tìm thấy resource → GlobalExceptionHandler trả HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
