package com.fastbank.fastbank.model.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
class ErrorResponseTest {

  private static final String API_PEOPLE = "/api/people";
  private static final String PATH = "/path";
  private static final String CANNOT_BE_BLANK = "cannot be blank";

  // ── int status constructor ────────────────────────────────────────────────

  @Test
  void intConstructorSetsAllFields() {
    Instant before = Instant.now();

    ErrorResponse response = new ErrorResponse(400, "Bad Request", "Invalid input", API_PEOPLE);

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.getError()).isEqualTo("Bad Request");
    assertThat(response.getMessage()).isEqualTo("Invalid input");
    assertThat(response.getPath()).isEqualTo(API_PEOPLE);
    assertThat(response.getErrorId()).isNotNull();
    assertThat(response.getTimestamp()).isAfterOrEqualTo(before);
    assertThat(response.getFieldErrors()).isNull();
  }

  // ── HttpStatus constructor ────────────────────────────────────────────────

  @Test
  void httpStatusConstructorDerivesStatusAndError() {
    ErrorResponse response =
        new ErrorResponse(HttpStatus.NOT_FOUND, "Person not found", "/api/people/123");

    assertThat(response.getStatus()).isEqualTo(404);
    assertThat(response.getError()).isEqualTo(HttpStatus.NOT_FOUND.toString());
    assertThat(response.getMessage()).isEqualTo("Person not found");
    assertThat(response.getPath()).isEqualTo("/api/people/123");
    assertThat(response.getFieldErrors()).isNull();
  }

  @Test
  void httpStatusConstructorGeneratesUniqueErrorIds() {
    ErrorResponse first = new ErrorResponse(HttpStatus.BAD_REQUEST, "msg", PATH);
    ErrorResponse second = new ErrorResponse(HttpStatus.BAD_REQUEST, "msg", PATH);

    assertThat(first.getErrorId()).isNotEqualTo(second.getErrorId());
  }

  @Test
  void httpStatusConstructorSetsTimestampToNow() {
    Instant before = Instant.now();

    ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "error", PATH);

    assertThat(response.getTimestamp()).isAfterOrEqualTo(before);
    assertThat(response.getTimestamp()).isBeforeOrEqualTo(Instant.now());
  }

  // ── HttpStatus + fieldErrors constructor ──────────────────────────────────

  @Test
  void fieldErrorsConstructorIncludesFieldErrors() {
    List<ErrorResponse.FieldError> fieldErrors =
        List.of(
            new ErrorResponse.FieldError("firstName", CANNOT_BE_BLANK),
            new ErrorResponse.FieldError("email", "must be a valid email"));

    ErrorResponse response =
        new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", API_PEOPLE, fieldErrors);

    assertThat(response.getStatus()).isEqualTo(400);
    assertThat(response.getFieldErrors()).hasSize(2);
    assertThat(response.getFieldErrors().get(0).getField()).isEqualTo("firstName");
    assertThat(response.getFieldErrors().get(0).getMessage()).isEqualTo(CANNOT_BE_BLANK);
    assertThat(response.getFieldErrors().get(1).getField()).isEqualTo("email");
    assertThat(response.getFieldErrors().get(1).getMessage()).isEqualTo("must be a valid email");
  }

  @Test
  void fieldErrorsConstructorWithEmptyListSetsEmptyFieldErrors() {
    ErrorResponse response =
        new ErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", API_PEOPLE, List.of());

    assertThat(response.getFieldErrors()).isEmpty();
  }

  // ── FieldError ────────────────────────────────────────────────────────────

  @Test
  void fieldErrorExposesFieldAndMessage() {
    ErrorResponse.FieldError fieldError = new ErrorResponse.FieldError("lastName", CANNOT_BE_BLANK);

    assertThat(fieldError.getField()).isEqualTo("lastName");
    assertThat(fieldError.getMessage()).isEqualTo(CANNOT_BE_BLANK);
  }

  // ── setters ───────────────────────────────────────────────────────────────

  @Test
  void settersOverrideConstructorValues() {
    ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST, "original", PATH);

    response.setMessage("updated message");
    response.setStatus(422);
    response.setPath("/new/path");

    assertThat(response.getMessage()).isEqualTo("updated message");
    assertThat(response.getStatus()).isEqualTo(422);
    assertThat(response.getPath()).isEqualTo("/new/path");
  }
}
