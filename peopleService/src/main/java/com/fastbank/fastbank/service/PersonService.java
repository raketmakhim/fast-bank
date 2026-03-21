package com.fastbank.fastbank.service;

import static java.util.stream.Collectors.toList;

import com.fastbank.fastbank.client.AccountServiceClient;
import com.fastbank.fastbank.model.dto.AccountResponse;
import com.fastbank.fastbank.model.dto.PersonRequest;
import com.fastbank.fastbank.model.dto.PersonResponse;
import com.fastbank.fastbank.model.entity.Person;
import com.fastbank.fastbank.repository.PersonRepository;
import com.fastbank.fastbank.utils.mapper.PersonMapper;
import com.fastbank.fastbank.utils.messagebroker.MessageSender;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service layer for managing {@link Person} business logic.
 *
 * <p>Handles person creation (including publishing a message via RabbitMQ) and retrieval of people
 * enriched with their associated accounts fetched from the Account Service.
 */
@Service
@Slf4j
public class PersonService {

  private final PersonRepository personRepository;
  private final MessageSender messageSender;
  private final AccountServiceClient accountServiceClient;

  /**
   * Constructs a {@code PersonService} with its required dependencies.
   *
   * @param personRepository     the repository for persisting {@link Person} entities
   * @param messageSender        the RabbitMQ message sender for publishing person events
   * @param accountServiceClient the Feign client for retrieving account data
   */
  public PersonService(
      PersonRepository personRepository,
      MessageSender messageSender,
      AccountServiceClient accountServiceClient) {
    this.personRepository = personRepository;
    this.messageSender = messageSender;
    this.accountServiceClient = accountServiceClient;
  }

  /**
   * Persists a new person and publishes a creation event to RabbitMQ.
   *
   * @param request the validated request containing the person's details
   * @return the saved {@link Person} entity
   */
  public Person savePerson(PersonRequest request) {
    log.info("Saving person: {} {}", request.getFirstName(), request.getLastName());
    Person person = PersonMapper.personToEntity(request);
    log.info(String.valueOf(person.getPersonId()));
    messageSender.sendMessage(person);
    return personRepository.save(person);
  }

  /**
   * Retrieves all people, each enriched with their associated accounts.
   *
   * <p>If the Account Service is unavailable, accounts default to an empty list.
   *
   * @return a list of {@link PersonResponse} objects with account data included
   */
  public List<PersonResponse> getPeople() {
    log.info("Fetching all people from the database");

    long startTime = System.currentTimeMillis();

    List<Person> people = personRepository.findAll();
    List<AccountResponse> accounts = getAccountsOrEmpty();

    List<PersonResponse> response =
        people.stream()
            .map(
                person ->
                    new PersonResponse(
                        person.getPersonId(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getEmail(),
                        accounts.stream()
                            .filter(a -> a.personId().equals(person.getPersonId()))
                            .collect(toList())))
            .toList();

    log.info(
        "Time taken to fetch people and accounts: {} ms", System.currentTimeMillis() - startTime);

    return response;
  }

  /**
   * Attempts to fetch all accounts from the Account Service, returning an empty list on failure.
   *
   * @return a list of {@link AccountResponse} objects, or an empty list if the service is down
   */
  private List<AccountResponse> getAccountsOrEmpty() {
    try {
      return accountServiceClient.getAllAccounts();
    } catch (RuntimeException e) {
      log.warn("Failed to fetch accounts: {}", e.getMessage());
      return Collections.emptyList();
    }
  }
}
