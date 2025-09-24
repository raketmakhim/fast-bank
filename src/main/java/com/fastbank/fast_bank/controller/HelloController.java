package com.fastbank.fast_bank.controller;

import com.fastbank.fast_bank.dto.PersonRequest;
import com.fastbank.fast_bank.entity.Person;
import com.fastbank.fast_bank.service.PersonService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {
    private final PersonService personService;

    public HelloController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, FastBank is running!";
    }

    @PostMapping("/person")
    public ResponseEntity<?> createPerson(@Valid @RequestBody PersonRequest personRequest) {
        Person person = personService.savePerson(personRequest);
        return ResponseEntity.ok("Person created: " + person.toString());
    }

    @GetMapping("/people")
    public List<Person> getPeople() {
        return personService.getPeople();
    }
}
