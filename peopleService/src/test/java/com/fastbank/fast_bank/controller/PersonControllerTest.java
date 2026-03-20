package com.fastbank.fast_bank.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fastbank.fast_bank.client.AccountServiceClient;
import com.fastbank.fast_bank.model.dto.AccountResponse;
import com.fastbank.fast_bank.model.dto.PersonResponse;
import com.fastbank.fast_bank.model.entity.Person;
import com.fastbank.fast_bank.service.PersonService;
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
class PersonControllerTest {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private PersonService personService;

    // MockBean to prevent Feign client from being wired during web slice context
    @MockitoBean private AccountServiceClient accountServiceClient;

    // ── GET /hello ────────────────────────────────────────────────────────────

    @Test
    void hello_shouldReturn200WithMessage() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, FastBank is running!"));
    }

    // ── GET /people ───────────────────────────────────────────────────────────

    @Test
    void getPeople_shouldReturn200WithList() throws Exception {
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

        mockMvc.perform(get("/people"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is("Jane")))
                .andExpect(jsonPath("$[0].lastName", is("Smith")))
                .andExpect(jsonPath("$[0].email", is("jane@example.com")))
                .andExpect(jsonPath("$[0].accounts", hasSize(1)));
    }

    @Test
    void getPeople_whenEmpty_shouldReturn200WithEmptyList() throws Exception {
        when(personService.getPeople()).thenReturn(List.of());

        mockMvc.perform(get("/people"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ── POST /person ──────────────────────────────────────────────────────────

    @Test
    void createPerson_withValidRequest_shouldReturn200() throws Exception {
        Person saved =
                Person.builder()
                        .personId(UUID.randomUUID())
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .build();
        when(personService.savePerson(any())).thenReturn(saved);

        mockMvc.perform(
                        post("/person")
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
    void createPerson_withBlankFirstName_shouldReturn400WithFieldError() throws Exception {
        mockMvc.perform(
                        post("/person")
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
    void createPerson_withBlankLastName_shouldReturn400WithFieldError() throws Exception {
        mockMvc.perform(
                        post("/person")
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
    void createPerson_withInvalidEmail_shouldReturn400WithFieldError() throws Exception {
        mockMvc.perform(
                        post("/person")
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
    void createPerson_withAllFieldsMissing_shouldReturn400WithMultipleFieldErrors()
            throws Exception {
        mockMvc.perform(post("/person").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors", hasSize(greaterThanOrEqualTo(3))));

        verify(personService, never()).savePerson(any());
    }
}
