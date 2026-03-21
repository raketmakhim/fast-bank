package com.fastbank.fastbank.client;

import com.fastbank.fastbank.model.dto.AccountResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Feign client for communicating with the Account Service.
 *
 * <p>Provides HTTP client methods to interact with the account-service running at {@code
 * http://localhost:8081}.
 */
@FeignClient(name = "account-service", url = "http://localhost:8081")
public interface AccountServiceClient {

  /**
   * Retrieves all accounts associated with a given person.
   *
   * @param personId the UUID of the person whose accounts are to be retrieved
   * @return a list of {@link AccountResponse} objects belonging to the specified person
   */
  @GetMapping("/api/accounts/personId}")
  List<AccountResponse> getAccountsByPersonId(UUID personId);

  /**
   * Retrieves all accounts in the system.
   *
   * @return a list of all {@link AccountResponse} objects
   */
  @GetMapping("/api/accounts")
  List<AccountResponse> getAllAccounts();
}
