package com.fastbank.fastbank.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fastbank.fastbank.client.AccountServiceClient;
import com.fastbank.fastbank.model.dto.AccountResponse;
import com.fastbank.fastbank.model.dto.PersonRequest;
import com.fastbank.fastbank.model.dto.PersonResponse;
import com.fastbank.fastbank.model.entity.Person;
import com.fastbank.fastbank.repository.PersonRepository;
import com.fastbank.fastbank.utils.messagebroker.MessageSender;
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
@SuppressWarnings("PMD.UnitTestContainsTooManyAsserts")
class PersonServiceTest {

  @Mock private PersonRepository personRepository;

  @Mock private MessageSender messageSender;

  @Mock private AccountServiceClient accountServiceClient;

  @InjectMocks private PersonService personService;

  private static final String FIRST_NAME = "John";
  private static final String LAST_NAME = "Doe";
  private static final String EMAIL = "john@example.com";
  private static final String JANE = "Jane";

  // ── savePerson ────────────────────────────────────────────────────────────

  @Test
  void savePersonMapsAndSends() {
    PersonRequest request = buildMockRequest(FIRST_NAME, LAST_NAME, EMAIL);
    Person saved = buildPerson(UUID.randomUUID(), FIRST_NAME, LAST_NAME, EMAIL);
    when(personRepository.save(any(Person.class))).thenReturn(saved);

    Person result = personService.savePerson(request);

    verify(messageSender).sendMessage(any(Person.class));
    verify(personRepository).save(any(Person.class));
    assertThat(result.getFirstName()).isEqualTo(FIRST_NAME);
    assertThat(result.getLastName()).isEqualTo(LAST_NAME);
    assertThat(result.getEmail()).isEqualTo(EMAIL);
  }

  @Test
  void savePersonWhenBrokerFailsPropagatesException() {
    PersonRequest request = buildMockRequest(FIRST_NAME, LAST_NAME, EMAIL);
    doThrow(new RuntimeException("broker unavailable"))
        .when(messageSender)
        .sendMessage(any(Person.class));

    assertThatThrownBy(() -> personService.savePerson(request))
        .isInstanceOf(RuntimeException.class);

    verify(personRepository, never()).save(any());
  }

  // ── getPeople ─────────────────────────────────────────────────────────────

  @Test
  void retrievePeopleReturnsWithMatchingAccounts() {
    UUID personId = UUID.randomUUID();
    Person person = buildPerson(personId, JANE, "Smith", "jane@example.com");
    AccountResponse account = buildAccount(personId);

    when(personRepository.findAll()).thenReturn(List.of(person));
    when(accountServiceClient.getAllAccounts()).thenReturn(List.of(account));

    List<PersonResponse> result = personService.getPeople();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).personId()).isEqualTo(personId);
    assertThat(result.get(0).firstName()).isEqualTo(JANE);
    assertThat(result.get(0).accounts()).hasSize(1);
  }

  @Test
  void retrievePeopleExcludesOtherPeoplesAccounts() {
    UUID personId = UUID.randomUUID();
    UUID otherPersonId = UUID.randomUUID();
    Person person = buildPerson(personId, JANE, "Smith", "jane@example.com");
    AccountResponse otherAccount = buildAccount(otherPersonId);

    when(personRepository.findAll()).thenReturn(List.of(person));
    when(accountServiceClient.getAllAccounts()).thenReturn(List.of(otherAccount));

    List<PersonResponse> result = personService.getPeople();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).accounts()).isEmpty();
  }

  @Test
  void retrievePeopleWhenAccountServiceFailsReturnsEmptyAccounts() {
    Person person = buildPerson(UUID.randomUUID(), JANE, "Smith", "jane@example.com");
    when(personRepository.findAll()).thenReturn(List.of(person));
    when(accountServiceClient.getAllAccounts())
        .thenThrow(new RuntimeException("service unavailable"));

    List<PersonResponse> result = personService.getPeople();

    assertThat(result).hasSize(1);
    assertThat(result.get(0).accounts()).isEmpty();
  }

  @Test
  void retrievePeopleWhenNoneExistReturnsEmptyList() {
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
