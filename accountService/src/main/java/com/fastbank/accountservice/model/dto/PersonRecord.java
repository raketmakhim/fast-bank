package com.fastbank.accountservice.model.dto;

import java.util.UUID;

/**
 * Data Transfer Object representing a person received from the People Service via RabbitMQ.
 *
 * @param firstName the person's first name
 * @param lastName  the person's last name
 * @param email     the person's email address
 * @param personId  the unique identifier of the person
 */
public record PersonRecord(String firstName, String lastName, String email, UUID personId) {}
