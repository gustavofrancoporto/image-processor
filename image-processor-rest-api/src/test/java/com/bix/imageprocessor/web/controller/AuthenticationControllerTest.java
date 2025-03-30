package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.config.SecurityConfiguration;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.security.service.JwtTokenService;
import com.bix.imageprocessor.web.dto.auth.AuthRequestDto;
import com.bix.imageprocessor.web.exception.service.impl.DefaultApiValidationErrorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
@Import({SecurityConfiguration.class, DefaultApiValidationErrorFactory.class})
@ActiveProfiles("test")
public class AuthenticationControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean UserService userService;
    @MockitoBean JwtTokenService jwtTokenService;

    @Mock User user;
    @Mock Jwt jwt;

    String email = "test@test.com";
    String password = "password";
    String token = "XYZ";

    @Test
    void shouldAuthenticateSuccessfully() throws Exception {

        when(userService.authenticate(email, password)).thenReturn(Optional.of(user));
        when(jwtTokenService.createToken(user)).thenReturn(jwt);
        when(jwt.getTokenValue()).thenReturn(token);

        var jsonBody = objectMapper.writeValueAsString(new AuthRequestDto(email, password));

        mockMvc.perform(post("/api/v1/login")
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        content().string(equalTo(token))
                );
    }

    @Test
    void shouldFailAuthenticationDueToInvalidCredentials() throws Exception {

        when(userService.authenticate(email, password)).thenReturn(Optional.empty());

        var jsonBody = objectMapper.writeValueAsString(new AuthRequestDto(email, password));

        mockMvc.perform(post("/api/v1/login")
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isUnauthorized(),
                        jsonPath("$.message", equalTo("Invalid email or password"))
                );
    }

    @ParameterizedTest
    @CsvSource({
            "             , 123456, email   ,     , NotBlank, field required",
            "test         , 123456, email   , test, Email   , must be an email",
            "test@test.com,       , password,     , NotBlank, field required",
    })
    void shouldFailAuthenticationDueToFieldValidationErrors(String email, String password, String fieldName, String rejectedValue, String errorCode, String message) throws Exception {

        var jsonBody = objectMapper.writeValueAsString(new AuthRequestDto(email, password));

        mockMvc.perform(post("/api/v1/login")
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message", equalTo("Validation error")),
                        jsonPath("$.validationErrors[0].object", equalTo("authRequestDto")),
                        jsonPath("$.validationErrors[0].field", equalTo(fieldName)),
                        jsonPath("$.validationErrors[0].rejectedValue", equalTo(rejectedValue)),
                        jsonPath("$.validationErrors[0].code", equalTo(errorCode)),
                        jsonPath("$.validationErrors[0].message", equalTo(message))
                );
    }
}
