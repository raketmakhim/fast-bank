package com.fastbank.fast_bank.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponse(
        UUID id,
        UUID personId,
        String accountNumber,
        String accountType,
        BigDecimal balance,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {}
