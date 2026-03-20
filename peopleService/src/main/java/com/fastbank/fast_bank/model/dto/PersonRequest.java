package com.fastbank.fast_bank.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Getter;

@Getter
public class PersonRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "firstName cannot be blank.")
    private String firstName;

    @NotBlank(message = "lastName cannot be blank.")
    private String lastName;

    @Email
    @NotBlank(message = "email cannot be blank.")
    private String email;
}
