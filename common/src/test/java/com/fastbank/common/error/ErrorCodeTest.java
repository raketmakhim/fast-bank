package com.fastbank.common.error;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;

/** Tests for {@link ErrorCode}. */
@SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
class ErrorCodeTest {

  @Test
  @DisplayName("maps each constant to its documented status")
  void mapsConstantsToStatuses() {
    assertThat(ErrorCode.ACCOUNT_NOT_FOUND.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(ErrorCode.PERSON_NOT_FOUND.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(ErrorCode.VALIDATION_FAILED.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(ErrorCode.DOWNSTREAM_UNAVAILABLE.getStatus())
        .isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    assertThat(ErrorCode.EMAIL_DELIVERY_FAILED.getStatus())
        .isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    assertThat(ErrorCode.INTERNAL_ERROR.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Test
  @DisplayName("derives a kebab-case type URI from the constant name")
  void derivesTypeUri() {
    assertThat(ErrorCode.ACCOUNT_NOT_FOUND.getType())
        .isEqualTo(URI.create("https://fastbank.dev/errors/account-not-found"));
    assertThat(ErrorCode.INTERNAL_ERROR.getType())
        .isEqualTo(URI.create("https://fastbank.dev/errors/internal-error"));
  }

  @Test
  @DisplayName("exposes the constant name as the stable client-facing code")
  void exposesConstantNameAsCode() {
    assertThat(ErrorCode.ACCOUNT_NOT_FOUND.getCode()).isEqualTo("ACCOUNT_NOT_FOUND");
  }

  @ParameterizedTest
  @EnumSource(ErrorCode.class)
  @DisplayName("every constant is fully populated")
  void everyConstantIsPopulated(ErrorCode code) {
    assertThat(code.getStatus()).isNotNull();
    assertThat(code.getTitle()).isNotBlank();
    assertThat(code.getCode()).isEqualTo(code.name());
    assertThat(code.getType().toString()).startsWith("https://fastbank.dev/errors/");
  }
}
