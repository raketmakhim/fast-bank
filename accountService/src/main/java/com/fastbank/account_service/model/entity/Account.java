package com.fastbank.account_service.model.entity;

import com.fastbank.account_service.model.enums.AccountStatus;
import com.fastbank.account_service.model.enums.AccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * JPA entity representing a bank account stored in the {@code accounts} table.
 *
 * <p>Each account is associated with a person via a logical foreign key ({@code personId}),
 * has a unique account number, a type, a balance, and a lifecycle status.
 * Timestamps are managed automatically via JPA lifecycle callbacks.
 */
@Entity
@Table(name = "accounts")
@Getter
@Setter
@ToString
public class Account {

  /** The unique identifier of the account, generated automatically. */
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  /** The UUID of the person who owns this account (logical foreign key). */
  @Column(name = "person_id", nullable = false)
  private UUID personId;

  /** The unique account number assigned to this account. */
  @Column(name = "account_number", unique = true, nullable = false)
  private String accountNumber;

  /** The type of this account (e.g. SAVINGS, CHECKING, BUSINESS). */
  @Column(name = "account_type", nullable = false)
  private AccountType accountType;

  /** The current balance of the account. Defaults to zero. */
  private BigDecimal balance = BigDecimal.ZERO;

  /** The current lifecycle status of the account. Defaults to {@link AccountStatus#ACTIVE}. */
  @Enumerated(EnumType.STRING)
  private AccountStatus status = AccountStatus.ACTIVE;

  /** The timestamp when the account was created. Not updatable after initial persist. */
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  /** The timestamp when the account was last updated. */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  /**
   * Sets {@code createdAt} and {@code updatedAt} to the current time before initial persistence.
   */
  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
  }

  /**
   * Updates {@code updatedAt} to the current time before each update.
   */
  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
