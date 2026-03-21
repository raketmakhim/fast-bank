package com.fastbank.fast_bank.model.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

/**
 * JPA entity representing a person stored in the {@code people} table.
 *
 * <p>Each person has a UUID primary key and holds basic identity information
 * (first name, last name, email).
 */
@Setter
@Entity
@Table(name = "people")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
public class Person {

  /** The unique identifier of the person. Not updatable after creation. */
  @Id
  @Column(name = "id", updatable = false, nullable = false)
  private UUID personId;

  /** The person's first name (max 50 characters). */
  @Column(name = "firstname", length = 50)
  private String firstName;

  /** The person's last name (max 50 characters). */
  @Column(name = "lastname", length = 50)
  private String lastName;

  /** The person's email address (max 100 characters). */
  @Column(name = "email", length = 100, unique = false)
  private String email;
}
