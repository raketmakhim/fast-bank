package com.fastbank.fastbank.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Data Transfer Object representing an account returned by the Account Service.
 *
 * @param id the unique identifier of the account
 * @param personId the UUID of the person who owns the account
 * @param accountNumber the account number string
 * @param accountType the type of account (e.g. SAVINGS, CURRENT)
 * @param balance the current balance of the account
 * @param status the status of the account (e.g. ACTIVE, CLOSED)
 * @param createdAt the timestamp when the account was created
 * @param updatedAt the timestamp when the account was last updated
 */
public record AccountResponse(
    UUID id,
    UUID personId,
    String accountNumber,
    String accountType,
    BigDecimal balance,
    String status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {}
