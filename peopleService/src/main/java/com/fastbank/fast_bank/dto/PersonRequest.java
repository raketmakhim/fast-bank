package com.fastbank.fast_bank.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PersonRequest {

    @NotBlank(message = "firstName cannot be blank.")
    private String firstName;

    @NotBlank(message = "lastName cannot be blank.")
    private String lastName;
}
