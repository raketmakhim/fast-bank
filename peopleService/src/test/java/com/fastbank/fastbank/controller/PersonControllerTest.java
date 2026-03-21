package com.fastbank.fastbank.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fastbank.fastbank.client.AccountServiceClient;
import com.fastbank.fastbank.model.dto.AccountResponse;
import com.fastbank.fastbank.model.dto.PersonResponse;
import com.fastbank.fastbank.model.entity.Person;
import com.fastbank.fastbank.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PersonController.class)
@SuppressWarnings({"PMD.ExcessiveImports", "PMD.UnitTestShouldIncludeAssert"})
class PersonControllerTest {

  private static final String PERSON_ENDPOINT = "/person";

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private PersonService personService;

  // MockBean to prevent Feign client from being wired during web slice context
  @MockitoBean private AccountServiceClient accountServiceClient;

  // ── GET /hello ────────────────────────────────────────────────────────────

  @Test
  void helloShouldReturn200WithMessage() throws Exception {
    mockMvc
        .perform(get("/hello"))
        .andExpect(status().isOk())
        .andExpect(content().string("Hello, FastBank is running!"));
  }

  // ── GET /people ───────────────────────────────────────────────────────────

  @Test
  void retrievePeopleReturns200WithList() throws Exception {
    UUID personId = UUID.randomUUID();
    AccountResponse account =
        new AccountResponse(
            UUID.randomUUID(),
            personId,
            "ACC-001",
            "SAVINGS",
            BigDecimal.TEN,
            "ACTIVE",
            LocalDateTime.now(),
            LocalDateTime.now());
    PersonResponse person =
        new PersonResponse(personId, "Jane", "Smith", "jane@example.com", List.of(account));
    when(personService.getPeople()).thenReturn(List.of(person));

    mockMvc
        .perform(get("/people"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].firstName", is("Jane")))
        .andExpect(jsonPath("$[0].lastName", is("Smith")))
        .andExpect(jsonPath("$[0].email", is("jane@example.com")))
        .andExpect(jsonPath("$[0].accounts", hasSize(1)));
  }

  @Test
  void retrievePeopleWhenEmptyReturns200EmptyList() throws Exception {
    when(personService.getPeople()).thenReturn(List.of());

    mockMvc.perform(get("/people")).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
  }

  // ── POST /person ──────────────────────────────────────────────────────────

  @Test
  void createPersonWithValidRequestReturns200() throws Exception {
    Person saved =
        Person.builder()
            .personId(UUID.randomUUID())
            .firstName("John")
            .lastName("Doe")
            .email("john@example.com")
            .build();
    when(personService.savePerson(any())).thenReturn(saved);

    mockMvc
        .perform(
            post(PERSON_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "firstName": "John",
                      "lastName": "Doe",
                      "email": "john@example.com"
                    }
                    """))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Person created:")));
  }

  @Test
  void createPersonWithBlankFirstNameReturns400WithFieldError() throws Exception {
    mockMvc
        .perform(
            post(PERSON_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "firstName": "",
                      "lastName": "Doe",
                      "email": "john@example.com"
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fieldErrors[*].field", hasItem("firstName")));

    verify(personService, never()).savePerson(any());
  }

  @Test
  void createPersonWithBlankLastNameReturns400WithFieldError() throws Exception {
    mockMvc
        .perform(
            post(PERSON_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "firstName": "John",
                      "lastName": "",
                      "email": "john@example.com"
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fieldErrors[*].field", hasItem("lastName")));

    verify(personService, never()).savePerson(any());
  }

  @Test
  void createPersonWithInvalidEmailReturns400WithFieldError() throws Exception {
    mockMvc
        .perform(
            post(PERSON_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                      "firstName": "John",
                      "lastName": "Doe",
                      "email": "not-an-email"
                    }
                    """))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fieldErrors[*].field", hasItem("email")));

    verify(personService, never()).savePerson(any());
  }

  @Test
  void createPersonWithAllFieldsMissingReturns400() throws Exception {
    mockMvc
        .perform(post(PERSON_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.fieldErrors", hasSize(greaterThanOrEqualTo(3))));

    verify(personService, never()).savePerson(any());
  }
}
