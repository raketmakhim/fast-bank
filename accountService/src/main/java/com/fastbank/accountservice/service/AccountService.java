package com.fastbank.accountservice.service;

import com.fastbank.accountservice.exception.AccountNotFoundException;
import com.fastbank.accountservice.model.dto.PersonRecord;
import com.fastbank.accountservice.model.entity.Account;
import com.fastbank.accountservice.model.enums.AccountStatus;
import com.fastbank.accountservice.model.enums.AccountType;
import com.fastbank.accountservice.repository.AccountRepository;
import com.fastbank.accountservice.utils.AccountNumberGenerator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

/**
 * Service layer for managing {@link Account} business logic.
 *
 * <p>Provides operations for creating, retrieving, updating, and querying accounts.
 */
@Service
public class AccountService {

  private final AccountRepository accountRepository;

  /**
   * Constructs an {@code AccountService} with the given {@link AccountRepository}.
   *
   * @param accountRepository the repository used to persist and query accounts
   */
  public AccountService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  /**
   * Retrieves all active accounts for a given person.
   *
   * @param personId the UUID of the person
   * @return a list of {@link Account} objects with {@link AccountStatus#ACTIVE} status
   */
  public List<Account> getActiveAccounts(UUID personId) {
    return accountRepository.findByPersonIdAndStatus(personId, AccountStatus.ACTIVE);
  }

  /**
   * Retrieves all accounts in the system.
   *
   * @return a list of all {@link Account} objects
   */
  public List<Account> getAllAccounts() {
    return accountRepository.findAll();
  }

  /**
   * Retrieves all accounts belonging to a given person regardless of status.
   *
   * @param personId the UUID of the person
   * @return a list of all {@link Account} objects for the given person
   */
  public List<Account> getAllAccountsByPersonId(UUID personId) {
    return accountRepository.findByPersonId(personId);
  }

  /**
   * Persists an account entity.
   *
   * @param account the {@link Account} to save
   * @return the saved {@link Account} entity
   */
  public Account saveAccount(Account account) {
    return accountRepository.save(account);
  }

  /**
   * Updates the status of an existing account.
   *
   * @param accountId the UUID of the account to update
   * @param status the new {@link AccountStatus} to apply
   * @return the updated {@link Account} entity
   * @throws AccountNotFoundException if no account exists with the given ID
   */
  public Account updateStatus(UUID accountId, AccountStatus status)
      throws AccountNotFoundException {
    Account account =
        accountRepository
            .findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

    account.setStatus(status);
    return accountRepository.save(account);
  }

  /**
   * Creates a default {@link AccountType#CHECKING} account for a newly registered person.
   *
   * @param person the {@link PersonRecord} containing the person's details
   * @return the newly created and persisted {@link Account}
   */
  public Account createAccountFromPerson(PersonRecord person) {
    Account account = new Account();
    account.setPersonId(person.personId());
    account.setAccountType(AccountType.CHECKING);
    account.setAccountNumber(AccountNumberGenerator.generate());

    accountRepository.save(account);
    return account;
  }
}
