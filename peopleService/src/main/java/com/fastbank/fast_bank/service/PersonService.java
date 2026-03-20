package com.fastbank.fast_bank.service;

import static java.util.stream.Collectors.toList;

import com.fastbank.fast_bank.client.AccountServiceClient;
import com.fastbank.fast_bank.model.dto.AccountResponse;
import com.fastbank.fast_bank.model.dto.PersonRequest;
import com.fastbank.fast_bank.model.dto.PersonResponse;
import com.fastbank.fast_bank.model.entity.Person;
import com.fastbank.fast_bank.repository.PersonRepository;
import com.fastbank.fast_bank.utils.mapper.PersonMapper;
import com.fastbank.fast_bank.utils.message_broker.MessageSender;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;
    private final MessageSender messageSender;
    private final AccountServiceClient accountServiceClient;

    public PersonService(
            PersonRepository personRepository,
            MessageSender messageSender,
            AccountServiceClient accountServiceClient) {
        this.personRepository = personRepository;
        this.messageSender = messageSender;
        this.accountServiceClient = accountServiceClient;
    }

    public Person savePerson(PersonRequest request) {
        log.info("Saving person: {} {}", request.getFirstName(), request.getLastName());
        Person person = PersonMapper.personToEntity(request);
        log.info(String.valueOf(person.getPersonId()));
        messageSender.sendMessage(person);
        return personRepository.save(person);
    }

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
                                                        .filter(
                                                                a ->
                                                                        a.personId()
                                                                                .equals(
                                                                                        person
                                                                                                .getPersonId()))
                                                        .collect(toList())))
                        .toList();

        log.info(
                "Time taken to fetch people and accounts: {} ms",
                System.currentTimeMillis() - startTime);

        return response;
    }

    private List<AccountResponse> getAccountsOrEmpty() {
        try {
            return accountServiceClient.getAllAccounts();
        } catch (RuntimeException e) {
            log.warn("Failed to fetch accounts: {}", e.getMessage());
            return Collections.emptyList();
        }
    }
}
