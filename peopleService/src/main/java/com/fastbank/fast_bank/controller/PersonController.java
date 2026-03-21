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

/**
 * REST controller for managing {@link Person} resources.
 *
 * <p>Exposes endpoints for creating and retrieving people in the FastBank system.
 */
@RestController
@Slf4j
public class PersonController {
  private final PersonService personService;

  /**
   * Constructs a {@code PersonController} with the given {@link PersonService}.
   *
   * @param personService the service used to handle person-related business logic
   */
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  /**
   * Health-check endpoint to verify the service is running.
   *
   * @return a greeting message confirming the service is active
   */
  @GetMapping("/hello")
  public String hello() {
    return "Hello, FastBank is running!";
  }

  /**
   * Creates a new person from the provided request body.
   *
   * @param personRequest the validated request containing the person's details
   * @return a {@link ResponseEntity} with a confirmation message and the created person's details
   */
  @PostMapping("/person")
  public ResponseEntity<?> createPerson(@Valid @RequestBody PersonRequest personRequest) {
    log.info("Received person creation request: {}", personRequest);
    Person person = personService.savePerson(personRequest);
    log.info("Person created successfully: {}", person);
    return ResponseEntity.ok("Person created: " + person.toString());
  }

  /**
   * Retrieves all people stored in the system.
   *
   * @return a {@link ResponseEntity} containing a list of {@link PersonResponse} objects
   */
  @GetMapping("/people")
  public ResponseEntity<List<PersonResponse>> getPeople() {
    log.info("Received request to get all people");
    List<PersonResponse> people = personService.getPeople();
    log.info("Returning {} people", people.size());
    return ResponseEntity.ok(people);
  }
}
