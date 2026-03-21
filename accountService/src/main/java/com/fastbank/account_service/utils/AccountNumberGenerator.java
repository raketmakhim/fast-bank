package com.fastbank.account_service.utils;

import java.util.UUID;

public class AccountNumberGenerator {
  public static String generate() {
    return UUID.randomUUID().toString().replace("-", "").substring(0, 20);
  }
}
