package com.fastbank.fast_bank.repository;

import com.fastbank.fast_bank.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // You get save(), findById(), findAll(), delete(), etc. for free
}
