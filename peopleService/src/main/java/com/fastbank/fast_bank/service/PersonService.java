package com.fastbank.fast_bank.service;

import com.fastbank.fast_bank.client.AccountServiceClient;
import com.fastbank.fast_bank.model.dto.AccountResponse;
import com.fastbank.fast_bank.model.dto.PersonRequest;
import com.fastbank.fast_bank.model.dto.PersonResponse;
import com.fastbank.fast_bank.model.entity.Person;
import com.fastbank.fast_bank.utils.message_broker.MessageSender;
import com.fastbank.fast_bank.repository.PersonRepository;
import com.fastbank.fast_bank.utils.mapper.PersonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class PersonService {

    private final PersonRepository personRepository;
    private final MessageSender messageSender;
    private final AccountServiceClient accountServiceClient;

    public PersonService(PersonRepository personRepository, MessageSender messageSender, AccountServiceClient accountServiceClient) {
        this.personRepository = personRepository;
        this.messageSender = messageSender;
        this.accountServiceClient = accountServiceClient;
    }

    public Person savePerson(PersonRequest request) throws JsonProcessingException {
        log.info("Saving person: {} {}", request.getFirstName(), request.getLastName());
        Person person = PersonMapper.personToEntity(request);
        log.info(String.valueOf(person.getPersonId()));
        messageSender.sendMessage(person);
        return personRepository.save(person);
    }
    
    public List<PersonResponse> getPeople(){
        log.info("Fetching all people from the database");

        long startTime = System.currentTimeMillis();

        List<Person> people = personRepository.findAll();

        List<AccountResponse> accounts = getAccountsOrEmpty();

        Map<UUID, List<AccountResponse>> accountsByPersonId = accounts.stream()
                .collect(Collectors.groupingBy(AccountResponse::personId));

        List<PersonResponse> response = people.stream()
                .map(person -> new PersonResponse(
                        person.getPersonId(),
                        person.getFirstName(),
                        person.getLastName(),
                        person.getEmail(),
                        accounts.stream().filter(a -> a.personId().equals(person.getPersonId())).collect(toList())
                        //accountsByPersonId.getOrDefault(person.getPersonId(), Collections.emptyList())
                ))
                .toList();

        long endTime = System.currentTimeMillis();

        System.out.println("Time taken to fetch people and accounts: " + (endTime - startTime) + " ms");

        return response;
    }

    private List<AccountResponse> getAccountsOrEmpty() {
        try {
            return accountServiceClient.getAllAccounts();
        } catch (Exception e) {
            log.warn("Failed to fetch accounts for person");
            return Collections.emptyList();
        }
    }

}
