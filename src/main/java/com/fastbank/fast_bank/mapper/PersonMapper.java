package com.fastbank.fast_bank.mapper;

import com.fastbank.fast_bank.dto.PersonRequest;
import com.fastbank.fast_bank.entity.Person;

public class PersonMapper {
    public static Person personToEntity(PersonRequest request) {
        return Person.builder()
            .firstName(request.getFirstName())
            .lastName(request.getFirstName())
            .build();
    }
}
