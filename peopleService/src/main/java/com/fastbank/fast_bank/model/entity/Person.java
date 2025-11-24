package com.fastbank.fast_bank.model.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

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
    @Column(name = "id", updatable = false, nullable = false)
    private UUID personId;

    @Column(name = "firstname", length = 50)
    private String firstName;

    @Column(name = "lastname", length = 50)
    private String lastName;

    @Column(name = "email", length = 100, unique = false)
    private String email;
}
