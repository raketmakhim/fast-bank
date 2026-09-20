package com.fastbank.common.error;

import java.net.URI;
import java.util.Locale;
import org.springframework.http.HttpStatus;

/**
 * Stable, machine-readable error codes shared by every FastBank service.
 *
 * <p>The constant name is the API contract: clients branch on it and it must not be renamed once
 * published. The {@code title} is human-readable prose and may change freely. Each constant also
 * carries the {@link HttpStatus} the error maps to and an RFC 9457 {@code type} URI derived from
 * the constant name.
 *
 * <p>Adding a new failure mode anywhere in the system means adding a constant here, which is what
 * keeps the services consistent with one another.
 */
public enum ErrorCode {

  /** A requested account does not exist. */
  ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "Account not found"),

  /** A requested person does not exist. */
  PERSON_NOT_FOUND(HttpStatus.NOT_FOUND, "Person not found"),

  /** The request body failed bean validation. */
  VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation failed"),

  /** A downstream service could not be reached or returned an unusable response. */
  DOWNSTREAM_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Downstream service unavailable"),

  /** An account notification email could not be delivered. */
  EMAIL_DELIVERY_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Email delivery failed"),

  /** Catch-all for unexpected failures; details are logged rather than returned. */
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

  private static final String TYPE_PREFIX = "https://fastbank.dev/errors/";

  private final HttpStatus status;
  private final String title;
  private final URI type;

  ErrorCode(HttpStatus status, String title) {
    this.status = status;
    this.title = title;
    this.type = URI.create(TYPE_PREFIX + name().toLowerCase(Locale.ROOT).replace('_', '-'));
  }

  /**
   * Returns the HTTP status this error maps to.
   *
   * @return the HTTP status
   */
  public HttpStatus getStatus() {
    return status;
  }

  /**
   * Returns the short human-readable summary of this error type.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the RFC 9457 {@code type} URI identifying this error type.
   *
   * @return the type URI
   */
  public URI getType() {
    return type;
  }

  /**
   * Returns the stable string code clients branch on.
   *
   * @return the constant name
   */
  public String getCode() {
    return name();
  }
}
