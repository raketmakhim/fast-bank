package com.fastbank.accountservice.service;

import com.fastbank.accountservice.email.EmailService;
import com.fastbank.accountservice.model.dto.PersonRecord;
import com.fastbank.accountservice.model.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service responsible for orchestrating account registration for a newly created person.
 *
 * <p>Creates a default account via {@link AccountService} when a person creation event is received.
 */
@Service
@Slf4j
public class AccountRegistrationService {
  private final AccountService accountService;
  private final EmailService emailService;

  /**
   * Constructs an {@code AccountRegistrationService} with the given dependencies.
   *
   * @param accountService the service used to create and persist the account
   * @param emailService the service used to send the account creation email
   */
  public AccountRegistrationService(AccountService accountService, EmailService emailService) {
    this.accountService = accountService;
    this.emailService = emailService;
  }

  /**
   * Registers a new default account for the given person.
   *
   * @param person the {@link PersonRecord} containing the person's details
   */
  public void registerAccount(PersonRecord person) {
    Account acc = accountService.createAccountFromPerson(person);
    log.info("Account created: {}", acc);
  }
}
