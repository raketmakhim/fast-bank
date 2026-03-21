package com.fastbank.accountservice.controller;

import com.fastbank.accountservice.exception.AccountNotFoundException;
import com.fastbank.accountservice.model.entity.Account;
import com.fastbank.accountservice.model.enums.AccountStatus;
import com.fastbank.accountservice.service.AccountService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link Account} resources.
 *
 * <p>Exposes endpoints under {@code /api/accounts} for creating, retrieving, and updating accounts
 * in the FastBank Account Service.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

  private final AccountService accountService;

  /**
   * Constructs an {@code AccountController} with the given {@link AccountService}.
   *
   * @param accountService the service used to handle account-related business logic
   */
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  /**
   * Updates the status of an existing account.
   *
   * @param accountId the UUID of the account to update
   * @param status the new {@link AccountStatus} to apply
   * @return a {@link ResponseEntity} containing the updated {@link Account}
   * @throws AccountNotFoundException if no account exists with the given ID
   */
  @PatchMapping("/{accountId}/status")
  public ResponseEntity<Account> updateAccountStatus(
      @PathVariable UUID accountId, @RequestParam AccountStatus status)
      throws AccountNotFoundException {
    Account account = accountService.updateStatus(accountId, status);
    return ResponseEntity.ok(account);
  }

  /**
   * Creates and persists a new account.
   *
   * @param account the {@link Account} to create
   * @return a {@link ResponseEntity} containing the saved {@link Account}
   */
  @PostMapping
  public ResponseEntity<Account> createAccount(@RequestBody Account account) {
    Account savedAccount = accountService.saveAccount(account);
    return ResponseEntity.ok(savedAccount);
  }

  /**
   * Retrieves all accounts in the system.
   *
   * @return a {@link ResponseEntity} containing a list of all {@link Account} objects
   */
  @GetMapping
  public ResponseEntity<List<Account>> getAllAccounts() {
    return ResponseEntity.ok(accountService.getAllAccounts());
  }

  /**
   * Retrieves all accounts associated with a given person ID.
   *
   * @param accountId the UUID of the person whose accounts are to be retrieved
   * @return a {@link ResponseEntity} containing a list of active {@link Account} objects
   */
  @GetMapping("/{personId}")
  public ResponseEntity<List<Account>> getAllAccountsByPersonId(@PathVariable UUID accountId) {
    return ResponseEntity.ok(accountService.getActiveAccounts(accountId));
  }

  /**
   * Retrieves all active accounts for a given person.
   *
   * @param personId the UUID of the person whose active accounts are to be retrieved
   * @return a {@link ResponseEntity} containing a list of active {@link Account} objects
   */
  @GetMapping("/active")
  public ResponseEntity<List<Account>> getActiveAccounts(@RequestParam UUID personId) {
    return ResponseEntity.ok(accountService.getActiveAccounts(personId));
  }
}
