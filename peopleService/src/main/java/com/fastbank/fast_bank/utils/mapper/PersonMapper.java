package com.fastbank.fast_bank.utils.mapper;

import com.fastbank.fast_bank.model.dto.PersonRequest;
import com.fastbank.fast_bank.model.entity.Person;
import java.util.UUID;

/**
 * Utility class for mapping between {@link PersonRequest} DTOs and {@link Person} entities.
 */
public class PersonMapper {

  /**
   * Converts a {@link PersonRequest} into a new {@link Person} entity with a generated UUID.
   *
   * @param request the request DTO containing the person's details
   * @return a new {@link Person} entity ready to be persisted
   */
  public static Person personToEntity(PersonRequest request) {
    return Person.builder()
        .personId(UUID.randomUUID())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .build();
  }
}
