package com.fastbank.fastbank.repository;

import com.fastbank.fastbank.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Person} entities.
 *
 * <p>Provides standard CRUD operations ({@code save}, {@code findById}, {@code findAll},
 * {@code delete}, etc.) inherited from {@link JpaRepository}.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {}
