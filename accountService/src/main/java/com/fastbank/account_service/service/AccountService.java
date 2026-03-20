package com.fastbank.account_service.service;

import com.fastbank.account_service.exception.AccountNotFoundException;
import com.fastbank.account_service.model.dto.PersonRecord;
import com.fastbank.account_service.model.entity.Account;
import com.fastbank.account_service.model.enums.AccountStatus;
import com.fastbank.account_service.model.enums.AccountType;
import com.fastbank.account_service.repository.AccountRepository;
import com.fastbank.account_service.utils.AccountNumberGenerator;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getActiveAccounts(UUID personId) {
        return accountRepository.findByPersonIdAndStatus(personId, AccountStatus.ACTIVE);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAllAccountsByPersonId(UUID personId) {
        return accountRepository.findByPersonId(personId);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateStatus(UUID accountId, AccountStatus status)
            throws AccountNotFoundException {
        Account account =
                accountRepository
                        .findById(accountId)
                        .orElseThrow(() -> new AccountNotFoundException(accountId));

        account.setStatus(status);
        return accountRepository.save(account);
    }

    public Account createAccountFromPerson(PersonRecord person) {
        Account account = new Account();
        account.setPersonId(person.personId());
        account.setAccountType(AccountType.CHECKING);
        account.setAccountNumber(AccountNumberGenerator.generate());

        accountRepository.save(account);
        return account;
    }
}
