package com.fastbank.accountservice.repository;

import com.fastbank.accountservice.model.entity.Account;
import com.fastbank.accountservice.model.enums.AccountStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for {@link Account} entities.
 *
 * <p>Extends {@link JpaRepository} to provide standard CRUD operations, plus custom queries
 * for filtering accounts by status and person.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

  /**
   * Finds all accounts with the given status.
   *
   * @param status the {@link AccountStatus} to filter by
   * @return a list of matching {@link Account} objects
   */
  List<Account> findByStatus(AccountStatus status);

  /**
   * Finds all accounts belonging to a person with the given status.
   *
   * @param personId the UUID of the person
   * @param status   the {@link AccountStatus} to filter by
   * @return a list of matching {@link Account} objects
   */
  List<Account> findByPersonIdAndStatus(UUID personId, AccountStatus status);

  /**
   * Finds all accounts belonging to a person regardless of status.
   *
   * @param personId the UUID of the person
   * @return a list of all {@link Account} objects for the given person
   */
  List<Account> findByPersonId(UUID personId);

  /**
   * Finds all accounts for a person that match the given status using a JPQL query.
   *
   * @param personId the UUID of the person
   * @param status   the {@link AccountStatus} to filter by
   * @return a list of matching {@link Account} objects
   */
  @Query("SELECT a FROM Account a WHERE a.personId = :personId AND a.status = :status")
  List<Account> findActiveAccountsForPerson(
      @Param("personId") UUID personId, @Param("status") AccountStatus status);
}
