package com.fastbank.fastbank.exception;

import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for the FastBank People Service.
 *
 * <p>Centralises exception handling across all controllers using {@link RestControllerAdvice}.
 * Exception handler methods should be added here to return consistent error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {}
