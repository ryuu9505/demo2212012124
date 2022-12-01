package com.example.demo5new.controller;

import com.example.demo5new.domain.user.Account;
import com.example.demo5new.repository.AccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    final String VALID_USERNAME = "user";
    final String VALID_EMAIL = "user@email.com";
    final String VALID_PASSWORD = "password";
    final String INVALID_USERNAME = "us";
    final String INVALID_EMAIL = "user";
    final String INVALID_PASSWORD = "passwor";

    @Test
    void signUpForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @DisplayName("Sign Up with invalid input")
    @Test
    void signUpSubmit_with_wrong_input() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("username", INVALID_USERNAME)
                        .param("email", VALID_EMAIL)
                        .param("password", VALID_PASSWORD)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
        mockMvc.perform(post("/signup")
                        .param("username", VALID_USERNAME)
                        .param("email", INVALID_EMAIL)
                        .param("password", VALID_PASSWORD)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
        mockMvc.perform(post("/signup")
                        .param("username", VALID_USERNAME)
                        .param("email", VALID_EMAIL)
                        .param("password", INVALID_PASSWORD)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @DisplayName("Sign Up with valid input")
    @Test
    void signUpSubmit_with_correct_input() throws Exception {
        mockMvc.perform(post("/signup")
                        .param("username", VALID_USERNAME)
                        .param("email", VALID_EMAIL)
                        .param("password", VALID_PASSWORD)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));

        // then
        Account accountByUsername = accountRepository.findByUsername(VALID_USERNAME);
        assertNotNull(accountByUsername);
        Account accountByEmail = accountRepository.findByEmail(VALID_EMAIL);
        assertNotNull(accountByEmail);
        assertEquals(accountByUsername, accountByEmail);
        // then; Is the password encoded?
        assertNotEquals(accountByUsername.getPassword(), VALID_PASSWORD);
    }

    @Test
    void loginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

}