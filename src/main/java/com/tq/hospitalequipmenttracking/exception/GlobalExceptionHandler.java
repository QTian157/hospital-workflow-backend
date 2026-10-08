package com.tq.hospitalequipmenttracking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 处理 ResponseStatusException（你现在用的）
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleException(BadRequestException ex) {

//        ErrorResponse response = new ErrorResponse(400, ex.getMessage());
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(
                ApiResponse.error(400, ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public  ResponseEntity<ApiResponse<Object>> handleNotFound(ResourceNotFoundException ex) {
        return new ResponseEntity<>(
                ApiResponse.error(404, ex.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneral(Exception ex) {
        ex.printStackTrace(); // key for debug
        return new ResponseEntity<>(ApiResponse.error(500, "Internal server error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // this is Spring has
    // When added  @Valid（or @Validated）on Controller，Spring will automatically do validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> validationErrors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(ApiResponse.error(400, "Validation failed", validationErrors), HttpStatus.BAD_REQUEST);
    }
}
