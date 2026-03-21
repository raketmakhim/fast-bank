package com.fastbank.account_service.controller;

import com.fastbank.account_service.exception.AccountNotFoundException;
import com.fastbank.account_service.model.entity.Account;
import com.fastbank.account_service.model.enums.AccountStatus;
import com.fastbank.account_service.service.AccountService;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  @PatchMapping("/{accountId}/status")
  public ResponseEntity<Account> updateAccountStatus(
      @PathVariable UUID accountId,
      @RequestParam AccountStatus status // Spring automatically converts from string
      ) throws AccountNotFoundException {
    Account account = accountService.updateStatus(accountId, status);
    return ResponseEntity.ok(account);
  }

  @PostMapping
  public ResponseEntity<Account> createAccount(@RequestBody Account account) {
    Account savedAccount = accountService.saveAccount(account);
    return ResponseEntity.ok(savedAccount);
  }

  @GetMapping
  public ResponseEntity<List<Account>> getAllAccounts() {
    return ResponseEntity.ok(accountService.getAllAccounts());
  }

  @GetMapping("/{personId}")
  public ResponseEntity<List<Account>> getAllAccountsByPersonId(@PathVariable UUID accountId) {
    return ResponseEntity.ok(accountService.getActiveAccounts(accountId));
  }

  @GetMapping("/active")
  public ResponseEntity<List<Account>> getActiveAccounts(@RequestParam UUID personId) {
    return ResponseEntity.ok(accountService.getActiveAccounts(personId));
  }
}
