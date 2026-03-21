package com.fastbank.account_service.utils;

import java.util.UUID;

/**
 * Utility class for generating unique account numbers.
 */
public class AccountNumberGenerator {

  /**
   * Generates a random 20-character alphanumeric account number derived from a UUID.
   *
   * @return a unique account number string of length 20
   */
  public static String generate() {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
  }
}
