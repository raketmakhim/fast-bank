package com.fastbank.account_service.model.dto;

import java.util.UUID;

public record PersonRecord(String firstName, String lastName, String email, UUID personId) {
}
