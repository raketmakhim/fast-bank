package com.fastbank.fast_bank.model.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ErrorResponseTest {

  // ── int status constructor ────────────────────────────────────────────────

  @Test
  void intConstructor_shouldSetAllFields() {
    Instant before = Instant.now();

    ErrorResponse response = new ErrorResponse(400, "Bad Request", "Invalid input", "/api/people");

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.getError()).isEqualTo("Bad Request");
    assertThat(response.getMessage()).isEqualTo("Invalid input");
    assertThat(response.getPath()).isEqualTo("/api/people");
    assertThat(response.getErrorId()).isNotNull();
    assertThat(response.getTimestamp()).isAfterOrEqualTo(before);
    assertThat(response.getFieldErrors()).isNull();
  }

  // ── HttpStatus constructor ────────────────────────────────────────────────

  @Test
  void httpStatusConstructor_shouldDeriveStatusAndError() {
    ErrorResponse response =
        new ErrorResponse(HttpStatus.NOT_FOUND, "Person not found", "/api/people/123");

    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getError()).isEqualTo(HttpStatus.NOT_FOUND.toString());
    assertThat(response.getMessage()).isEqualTo("Person not found");
    assertThat(response.getPath()).isEqualTo("/api/people/123");
    assertThat(response.getFieldErrors()).isNull();
  }

  @Test
  void httpStatusConstructor_shouldGenerateUniqueErrorIds() {
    ErrorResponse first = new ErrorResponse(HttpStatus.BAD_REQUEST, "msg", "/path");
    ErrorResponse second = new ErrorResponse(HttpStatus.BAD_REQUEST, "msg", "/path");

    assertThat(first.getErrorId()).isNotEqualTo(second.getErrorId());
  }

  @Test
  void httpStatusConstructor_shouldSetTimestampToNow() {
    Instant before = Instant.now();

    ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error", "/path");

    assertThat(response.getTimestamp()).isAfterOrEqualTo(before);
    assertThat(response.getTimestamp()).isBeforeOrEqualTo(Instant.now());
  }

  // ── HttpStatus + fieldErrors constructor ──────────────────────────────────

  @Test
  void fieldErrorsConstructor_shouldIncludeFieldErrors() {
    List<ErrorResponse.FieldError> fieldErrors =
        List.of(
            new ErrorResponse.FieldError("firstName", "cannot be blank"),
            new ErrorResponse.FieldError("email", "must be a valid email"));

    ErrorResponse response =
        new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", "/api/people", fieldErrors);

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.getFieldErrors()).hasSize(2);
    assertThat(response.getFieldErrors().get(0).getField()).isEqualTo("firstName");
    assertThat(response.getFieldErrors().get(0).getMessage()).isEqualTo("cannot be blank");
    assertThat(response.getFieldErrors().get(1).getField()).isEqualTo("email");
    assertThat(response.getFieldErrors().get(1).getMessage()).isEqualTo("must be a valid email");
  }

  @Test
  void fieldErrorsConstructor_withEmptyList_shouldSetEmptyFieldErrors() {
    ErrorResponse response =
        new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", "/api/people", List.of());

    assertThat(response.getFieldErrors()).isEmpty();
  }

  // ── FieldError ────────────────────────────────────────────────────────────

  @Test
  void fieldError_shouldExposeFieldAndMessage() {
    ErrorResponse.FieldError fieldError =
        new ErrorResponse.FieldError("lastName", "cannot be blank");

    assertThat(fieldError.getField()).isEqualTo("lastName");
    assertThat(fieldError.getMessage()).isEqualTo("cannot be blank");
  }

  // ── setters ───────────────────────────────────────────────────────────────

  @Test
  void setters_shouldOverrideConstructorValues() {
    ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, "original", "/path");

    response.setMessage("updated message");
    response.setStatus(422);
    response.setPath("/new/path");

    assertThat(response.getMessage()).isEqualTo("updated message");
    assertThat(response.getStatus()).isEqualTo(422);
    assertThat(response.getPath()).isEqualTo("/new/path");
  }
}
