package com.fastbank.fast_bank.utils.mapper;

import com.fastbank.fast_bank.model.dto.PersonRequest;
import com.fastbank.fast_bank.model.entity.Person;

import java.util.UUID;

public class PersonMapper {
    public static Person personToEntity(PersonRequest request) {
        return Person.builder()
            .personId(UUID.randomUUID())
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .email(request.getEmail())
            .build();
    }
}
