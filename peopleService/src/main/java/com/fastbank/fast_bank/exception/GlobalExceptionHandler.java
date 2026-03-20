package com.fastbank.fast_bank.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //    @ExceptionHandler(Exception.class)
    //    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    //        // Fallback for uncaught exceptions
    //        ErrorResponse e = new ErrorResponse();
    //        e.setMessage("An unexpected error occurred: " + ex.getMessage());
    //        e.setErrorCode("INTERNAL_SERVER_ERROR");
    //        e.setTimestamp(java.time.LocalDateTime.now());
    //        return ResponseEntity.internalServerError().body(e);
    //    }
}
