package com.fastbank.fast_bank.model.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * Standard error response body returned by exception handlers.
 *
 * <p>Includes a unique error ID, HTTP status, human-readable message, request path, timestamp,
 * and an optional list of per-field validation errors.
 */
@Setter
@Getter
public class ErrorResponse {
  private String message;
  private String error;
  private int status;
  private Instant timestamp;
  private List<FieldError> fieldErrors;
  private String path;
  private UUID errorId;

  /**
   * Constructs an {@code ErrorResponse} with a raw status code and error label.
   *
   * @param status  the HTTP status code
   * @param error   a short error label
   * @param message a human-readable description of the error
   * @param path    the request URI that caused the error
   */
  public ErrorResponse(int status, String error, String message, String path) {
    this.errorId = UUID.randomUUID();
    this.timestamp = Instant.now();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }

  /**
   * Constructs an {@code ErrorResponse} from a {@link HttpStatus}.
   *
   * @param status  the HTTP status
   * @param message a human-readable description of the error
   * @param path    the request URI that caused the error
   */
  public ErrorResponse(HttpStatus status, String message, String path) {
    this.errorId = UUID.randomUUID();
    this.timestamp = Instant.now();
    this.status = status.value();
    this.error = status.toString();
    this.message = message;
    this.path = path;
  }

  /**
   * Constructs an {@code ErrorResponse} with per-field validation errors.
   *
   * @param status      the HTTP status
   * @param message     a human-readable description of the error
   * @param path        the request URI that caused the error
   * @param fieldErrors a list of field-level validation errors
   */
  public ErrorResponse(
      HttpStatus status, String message, String path, List<FieldError> fieldErrors) {
    this(status, message, path);
    this.fieldErrors = fieldErrors;
  }

  /**
   * Represents a single field-level validation error.
   */
  @Getter
  public static class FieldError {
    private final String field;
    private final String message;

    /**
     * Constructs a {@code FieldError} for the given field and message.
     *
     * @param field   the name of the field that failed validation
     * @param message the validation error message
     */
    public FieldError(String field, String message) {
      this.field = field;
      this.message = message;
    }
  }
}
