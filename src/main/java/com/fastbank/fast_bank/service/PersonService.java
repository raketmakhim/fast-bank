package com.fastbank.fast_bank.service;

import com.fastbank.fast_bank.dto.PersonRequest;
import com.fastbank.fast_bank.entity.Person;
import com.fastbank.fast_bank.repository.PersonRepository;
import com.fastbank.fast_bank.mapper.PersonMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person savePerson(PersonRequest request) {
        return personRepository.save(PersonMapper.personToEntity(request));
    }
    
    public List<Person> getPeople(){
        return personRepository.findAll();
    }

}
