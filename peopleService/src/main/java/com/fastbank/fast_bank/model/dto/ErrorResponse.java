package com.fastbank.fast_bank.model.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

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

  public ErrorResponse(int status, String error, String message, String path) {
    this.errorId = UUID.randomUUID();
    this.timestamp = Instant.now();
    this.status = status;
    this.error = error;
    this.message = message;
    this.path = path;
  }

  public ErrorResponse(HttpStatus status, String message, String path) {
    this.errorId = UUID.randomUUID();
    this.timestamp = Instant.now();
    this.status = status.value();
    this.error = status.toString();
    this.message = message;
    this.path = path;
  }

  public ErrorResponse(
      HttpStatus status, String message, String path, List<FieldError> fieldErrors) {
    this(status, message, path);
    this.fieldErrors = fieldErrors;
  }

  @Getter
  public static class FieldError {
    private final String field;
    private final String message;

    public FieldError(String field, String message) {
      this.field = field;
      this.message = message;
    }
  }
}
