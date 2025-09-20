package com.ptit.thesis.smartrecruit.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<?> handleInvalidTokenException(InvalidTokenException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .statusCode(HttpStatus.UNAUTHORIZED.value())
            .error("Unauthorized")
            .path(request.getDescription(false).replace("uri=", ""))
            .message(ex.getMessage())
            .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Xử lý các lỗi validation phổ biến như @Valid DTO, @RequestParam thiếu, ...
     * Trả về HTTP status 400 BAD_REQUEST.
     * @param ex Exception được ném ra (có thể là một trong nhiều loại).
     * @param request Request gây ra exception.
     * @return một ResponseEntity chứa thông tin lỗi chi tiết.
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, 
        MissingServletRequestParameterException.class, ConstraintViolationException.class})
    public ResponseEntity<?> handleValidationException(Exception ex, WebRequest request) {
        String message;
        
        if (ex instanceof MethodArgumentNotValidException) {
            message = ((MethodArgumentNotValidException) ex).getBindingResult()
                .getFieldErrors().stream()
                .map(error -> "'" + error.getField() + "': " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        } else if (ex instanceof ConstraintViolationException) {
            message = ((ConstraintViolationException) ex).getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));
        } else { // MissingServletRequestParameterException
            message = ex.getMessage();
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .path(request.getDescription(false).replace("uri=", ""))
            .error("Validation error")
            .message(message)
            .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
