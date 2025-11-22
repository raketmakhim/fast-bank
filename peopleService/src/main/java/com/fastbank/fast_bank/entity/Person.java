package com.fastbank.fast_bank.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Setter
@Entity
@Table(name = "people")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Getter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL maps to identity
    private Long id;

    @Column(name = "firstname", length = 50)
    private String firstName;

    @Column(name = "lastname", length = 50)
    private String lastName;

    @Column(name = "email", length = 100, unique = true)
    private String email;
}
