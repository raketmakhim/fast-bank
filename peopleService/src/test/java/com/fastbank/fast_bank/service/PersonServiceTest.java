package com.fastbank.fast_bank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fastbank.fast_bank.client.AccountServiceClient;
import com.fastbank.fast_bank.model.dto.AccountResponse;
import com.fastbank.fast_bank.model.dto.PersonRequest;
import com.fastbank.fast_bank.model.dto.PersonResponse;
import com.fastbank.fast_bank.model.entity.Person;
import com.fastbank.fast_bank.repository.PersonRepository;
import com.fastbank.fast_bank.utils.message_broker.MessageSender;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

  @Mock private PersonRepository personRepository;

  @Mock private MessageSender messageSender;

  @Mock private AccountServiceClient accountServiceClient;

  @InjectMocks private PersonService personService;

  // ── savePerson ────────────────────────────────────────────────────────────

  @Test
  void savePerson_shouldMapRequestSendMessageAndSave() {
    PersonRequest request = buildMockRequest("John", "Doe", "john@example.com");
    Person saved = buildPerson(UUID.randomUUID(), "John", "Doe", "john@example.com");
    when(personRepository.save(any(Person.class))).thenReturn(saved);

    Person result = personService.savePerson(request);

    verify(messageSender).sendMessage(any(Person.class));
    verify(personRepository).save(any(Person.class));
    assertThat(result.getFirstName()).isEqualTo("John");
    assertThat(result.getLastName()).isEqualTo("Doe");
    assertThat(result.getEmail()).isEqualTo("john@example.com");
  }

  @Test
  void savePerson_whenMessageSenderThrows_shouldPropagateAndNotSave() {
    PersonRequest request = buildMockRequest("John", "Doe", "john@example.com");
    doThrow(new RuntimeException("broker unavailable"))
        .when(messageSender)
        .sendMessage(any(Person.class));

    assertThatThrownBy(() -> personService.savePerson(request))
        .isInstanceOf(RuntimeException.class);

    verify(personRepository, never()).save(any());
  }

  // ── getPeople ─────────────────────────────────────────────────────────────

  @Test
  void getPeople_shouldReturnPeopleWithTheirAccounts() {
    UUID personId = UUID.randomUUID();
    Person person = buildPerson(personId, "Jane", "Smith", "jane@example.com");
    AccountResponse account = buildAccount(personId);

    when(personRepository.findAll()).thenReturn(List.of(person));
    when(accountServiceClient.getAllAccounts()).thenReturn(List.of(account));

    List<PersonResponse> result = personService.getPeople();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).personId()).isEqualTo(personId);
    assertThat(result.get(0).firstName()).isEqualTo("Jane");
    assertThat(result.get(0).accounts()).hasSize(1);
  }

  @Test
  void getPeople_shouldNotIncludeAccountsBelongingToOtherPeople() {
    UUID personId = UUID.randomUUID();
    UUID otherPersonId = UUID.randomUUID();
    Person person = buildPerson(personId, "Jane", "Smith", "jane@example.com");
    AccountResponse otherAccount = buildAccount(otherPersonId);

    when(personRepository.findAll()).thenReturn(List.of(person));
    when(accountServiceClient.getAllAccounts()).thenReturn(List.of(otherAccount));

    List<PersonResponse> result = personService.getPeople();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).accounts()).isEmpty();
  }

  @Test
  void getPeople_whenAccountServiceFails_shouldReturnPeopleWithEmptyAccounts() {
    Person person = buildPerson(UUID.randomUUID(), "Jane", "Smith", "jane@example.com");
    when(personRepository.findAll()).thenReturn(List.of(person));
    when(accountServiceClient.getAllAccounts())
        .thenThrow(new RuntimeException("service unavailable"));

    List<PersonResponse> result = personService.getPeople();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).accounts()).isEmpty();
  }

  @Test
  void getPeople_whenNoPeopleExist_shouldReturnEmptyList() {
    when(personRepository.findAll()).thenReturn(List.of());
    when(accountServiceClient.getAllAccounts()).thenReturn(List.of());

    List<PersonResponse> result = personService.getPeople();

    assertThat(result).isEmpty();
  }

  // ── helpers ───────────────────────────────────────────────────────────────

  private PersonRequest buildMockRequest(String firstName, String lastName, String email) {
    PersonRequest request = mock(PersonRequest.class);
    when(request.getFirstName()).thenReturn(firstName);
    when(request.getLastName()).thenReturn(lastName);
    when(request.getEmail()).thenReturn(email);
    return request;
  }

  private Person buildPerson(UUID id, String firstName, String lastName, String email) {
    return Person.builder()
        .personId(id)
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .build();
  }

  private AccountResponse buildAccount(UUID personId) {
    return new AccountResponse(
        UUID.randomUUID(),
        personId,
        "ACC-001",
        "SAVINGS",
        BigDecimal.TEN,
        "ACTIVE",
        LocalDateTime.now(),
        LocalDateTime.now());
  }
}
