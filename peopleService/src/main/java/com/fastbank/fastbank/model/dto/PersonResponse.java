package com.fastbank.fastbank.model.dto;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object representing a person returned by the People Service.
 *
 * @param personId  the unique identifier of the person
 * @param firstName the person's first name
 * @param lastName  the person's last name
 * @param email     the person's email address
 * @param accounts  the list of accounts associated with this person
 */
public record PersonResponse(
    UUID personId, String firstName, String lastName, String email, List<?> accounts) {}
