package com.fastbank.fast_bank.service;

import com.fastbank.fast_bank.dto.PersonRequest;
import com.fastbank.fast_bank.entity.Person;
import com.fastbank.fast_bank.message_broker.MessageSender;
import com.fastbank.fast_bank.repository.PersonRepository;
import com.fastbank.fast_bank.mapper.PersonMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PersonService {

    @Autowired
    MessageSender messageSender;

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person savePerson(PersonRequest request) throws JsonProcessingException {
        log.info("Saving person: {} {}", request.getFirstName(), request.getLastName());
        messageSender.sendMessage(request);
        return personRepository.save(PersonMapper.personToEntity(request));
    }
    
    public List<Person> getPeople(){
        log.info("Fetching all people from the database");
        return personRepository.findAll();
    }

}
