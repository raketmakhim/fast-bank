package com.fastbank.account_service.repository;

import com.fastbank.account_service.model.entity.Account;
import com.fastbank.account_service.model.enums.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {

    List<Account> findByStatus(AccountStatus status);

    List<Account> findByPersonIdAndStatus(UUID personId, AccountStatus status);

    List<Account> findByPersonId(UUID personId);

    @Query("SELECT a FROM Account a WHERE a.personId = :personId AND a.status = :status")
    List<Account> findActiveAccountsForPerson(
            @Param("personId") UUID personId,
            @Param("status") AccountStatus status
    );
}
