package com.fastbank.fastbank.exception;

import com.fastbank.fastbank.model.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handles validation exceptions thrown by controller method argument binding.
 *
 * <p>Intercepts {@link MethodArgumentNotValidException} and returns a structured {@link
 * ErrorResponse} containing per-field validation error details.
 */
@RestControllerAdvice
public class ValidationExceptionHandler {

  /**
   * Handles {@link MethodArgumentNotValidException} thrown when a request body fails validation.
   *
   * @param ex the exception containing binding result with field errors
   * @param request the HTTP request that triggered the validation failure
   * @return a {@link ResponseEntity} with HTTP 400 and an {@link ErrorResponse} body
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    List<ErrorResponse.FieldError> fieldErrors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(err -> new ErrorResponse.FieldError(err.getField(), err.getDefaultMessage()))
            .toList();

    ErrorResponse response =
        new ErrorResponse(
            HttpStatus.BAD_REQUEST, "Validation failed", request.getRequestURI(), fieldErrors);

    return ResponseEntity.badRequest().body(response);
  }
}
