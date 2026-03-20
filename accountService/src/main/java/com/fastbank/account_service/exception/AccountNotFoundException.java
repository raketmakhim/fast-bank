package com.fastbank.account_service.exception;

import java.util.UUID;

public class AccountNotFoundException extends Exception {
  public AccountNotFoundException(UUID id) {
    super("Account with ID " + id.toString() + " not found.");
  }
}
