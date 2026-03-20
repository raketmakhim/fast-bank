package com.fastbank.fast_bank.client;

import com.fastbank.fast_bank.model.dto.AccountResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "account-service", url = "http://localhost:8081")
public interface AccountServiceClient {

    @GetMapping("/api/accounts/personId}")
    List<AccountResponse> getAccountsByPersonId(UUID personId);

    @GetMapping("/api/accounts")
    List<AccountResponse> getAllAccounts();
}
