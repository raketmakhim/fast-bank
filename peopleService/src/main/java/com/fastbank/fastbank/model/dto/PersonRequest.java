package com.fastbank.fastbank.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import lombok.Getter;

/**
 * Request DTO for creating a new person.
 *
 * <p>All fields are validated on receipt; blank or missing values will produce a 400 response.
 */
@Getter
public class PersonRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  /** The person's first name. Must not be blank. */
  @NotBlank(message = "firstName cannot be blank.")
  private String firstName;

  /** The person's last name. Must not be blank. */
  @NotBlank(message = "lastName cannot be blank.")
  private String lastName;

  /** The person's email address. Must be a valid email format and must not be blank. */
  @Email
  @NotBlank(message = "email cannot be blank.")
  private String email;
}
