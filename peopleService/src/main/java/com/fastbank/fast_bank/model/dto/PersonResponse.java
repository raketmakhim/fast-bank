package com.fastbank.fast_bank.model.dto;

import java.util.List;
import java.util.UUID;

public record PersonResponse(
        UUID personId, String firstName, String lastName, String email, List<?> accounts) {}
