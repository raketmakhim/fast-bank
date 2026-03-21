package com.fastbank.account_service.exception;

import java.util.UUID;

/**
 * Exception thrown when an {@link com.fastbank.account_service.model.entity.Account} with the
 * requested ID cannot be found in the system.
 */
public class AccountNotFoundException extends Exception {

  /**
   * Constructs an {@code AccountNotFoundException} for the given account ID.
   *
   * @param id the UUID of the account that was not found
   */
  public AccountNotFoundException(UUID id) {
    super("Account with ID " + id.toString() + " not found.");
  }
}
