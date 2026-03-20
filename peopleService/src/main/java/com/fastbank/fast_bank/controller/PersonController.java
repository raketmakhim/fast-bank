package com.fastbank.fast_bank.controller;

import com.fastbank.fast_bank.model.dto.PersonRequest;
import com.fastbank.fast_bank.model.dto.PersonResponse;
import com.fastbank.fast_bank.model.entity.Person;
import com.fastbank.fast_bank.service.PersonService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PersonController {
  private final PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @GetMapping("/hello")
  public String hello() {
    return "Hello, FastBank is running!";
  }

  @PostMapping("/person")
  public ResponseEntity<?> createPerson(@Valid @RequestBody PersonRequest personRequest) {
    log.info("Received person creation request: {}", personRequest);
    Person person = personService.savePerson(personRequest);
    log.info("Person created successfully: {}", person);
    return ResponseEntity.ok("Person created: " + person.toString());
  }

  @GetMapping("/people")
  public ResponseEntity<List<PersonResponse>> getPeople() {
    log.info("Received request to get all people");
    List<PersonResponse> people = personService.getPeople();
    log.info("Returning {} people", people.size());
    return ResponseEntity.ok(people);
  }
}
