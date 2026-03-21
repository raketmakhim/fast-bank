package com.fastbank.account_service.model.enums;

/**
 * Represents the lifecycle status of a bank account.
 */
public enum AccountStatus {
  /** The account is open and fully operational. */
  ACTIVE,
  /** The account exists but is not currently in use. */
  INACTIVE,
  /** The account has been permanently closed. */
  CLOSED,
  /** The account has been temporarily suspended. */
  SUSPENDED,
  /** The account is awaiting approval or activation. */
  PENDING
}
