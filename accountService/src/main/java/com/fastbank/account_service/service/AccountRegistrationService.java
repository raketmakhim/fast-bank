package com.fastbank.account_service.service;

import com.fastbank.account_service.email.EmailService;
import com.fastbank.account_service.model.dto.PersonRecord;
import com.fastbank.account_service.model.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountRegistrationService {
  private final AccountService accountService;
  private final EmailService emailService;

  public AccountRegistrationService(AccountService accountService, EmailService emailService) {
    this.accountService = accountService;
    this.emailService = emailService;
  }

  public void registerAccount(PersonRecord person) {
    Account acc = accountService.createAccountFromPerson(person);
    log.info("Account created: {}", acc);
  }
}
