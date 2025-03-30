package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.config.SecurityConfiguration;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import com.bix.imageprocessor.web.dto.user.UserDto;
import com.bix.imageprocessor.web.exception.model.NoChangeRequiredException;
import com.bix.imageprocessor.web.exception.model.ApiError;
import com.bix.imageprocessor.web.exception.model.ApiValidationError;
import com.bix.imageprocessor.web.exception.service.impl.DefaultApiValidationErrorFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static com.bix.imageprocessor.domain.subscription.model.SubscriptionType.PREMIUM;
import static io.micrometer.common.util.StringUtils.isNotEmpty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({SecurityConfiguration.class, DefaultApiValidationErrorFactory.class})
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean UserService userService;

    @Mock User user;

    long userId = 2L;
    UserDto adminUserDto = UserDto.builder().isAdmin(true).build();
    UserDto simpleUserDto = UserDto.builder()
            .name("Simple User")
            .email("simple@user.com")
            .isPremium(true)
            .isAdmin(false)
            .build();
    CreateUserDto newUserDto = CreateUserDto.builder()
            .name("Simple User")
            .email("simple@user.com")
            .password("123456")
            .build();

    @Test
    void shouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {

        mockMvc.perform(get("/api/v1/users/" + userId))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnInternalServerErrorWhenUserNotFound() throws Exception {

        when(userService.findById(userId)).thenReturn(Optional.empty());

        var jwt = jwt().jwt(builder -> builder.subject(String.valueOf(userId)));

        mockMvc.perform(get("/api/v1/users/" + userId).with(jwt))
                .andExpectAll(
                        status().isInternalServerError(),
                        jsonPath("$.message", equalTo("Internal server error"))
                );
    }

    @Test
    void shouldAllowNonAdminUserToAccessOwnInformation() throws Exception {

        when(userService.findById(userId)).thenReturn(Optional.of(simpleUserDto));

        var jwt = jwt().jwt(builder -> builder.subject(String.valueOf(userId)));

        mockMvc.perform(get("/api/v1/users/" + userId).with(jwt))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.email", equalTo(simpleUserDto.email())),
                        jsonPath("$.name", equalTo(simpleUserDto.name())),
                        jsonPath("$.isPremium", equalTo(simpleUserDto.isPremium())),
                        jsonPath("$.isAdmin", equalTo(simpleUserDto.isAdmin()))
                );
    }

    @Test
    void shouldForbidNonAdminUserFromAccessingOtherUserInformation() throws Exception {

        var anotherUserId = 3L;

        when(userService.findById(userId)).thenReturn(Optional.of(simpleUserDto));

        var jwt = jwt().jwt(builder -> builder.subject(String.valueOf(userId)));

        mockMvc.perform(get("/api/v1/users/" + anotherUserId).with(jwt))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowAdminUserToAccessOtherUserInformation() throws Exception {

        var currentUserId = 3L;

        when(userService.findById(currentUserId)).thenReturn(Optional.of(adminUserDto));
        when(userService.findById(userId)).thenReturn(Optional.of(simpleUserDto));

        var jwt = jwt().jwt(builder -> builder.subject(String.valueOf(userId)));

        mockMvc.perform(get("/api/v1/users/" + userId).with(jwt))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.email", equalTo(simpleUserDto.email())),
                        jsonPath("$.name", equalTo(simpleUserDto.name())),
                        jsonPath("$.isPremium", equalTo(simpleUserDto.isPremium())),
                        jsonPath("$.isAdmin", equalTo(simpleUserDto.isAdmin())),
                        jsonPath("$.password").doesNotExist()
                );
    }

    @Test
    void shouldCreateUser() throws Exception {

        when(userService.create(newUserDto)).thenReturn(user);
        when(user.getId()).thenReturn(userId);

        var jsonBody = objectMapper.writeValueAsString(newUserDto);

        mockMvc.perform(post("/api/v1/users")
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isCreated(),
                        header().string("location", equalTo("http://localhost/api/v1/users/" + userId))
                );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  "})
    void shouldReturnValidationErrorsWhenCreatingUsersWIthRequiredFieldsNullOrBlank(String value) throws Exception {

        var jsonBody = objectMapper.writeValueAsString(new CreateUserDto(value, value, value));

        var expectedValidationErrors = new ArrayList<ApiValidationError>();
        expectedValidationErrors.add(new ApiValidationError("createUserDto", "email", value, "NotBlank", "field required"));
        expectedValidationErrors.add(new ApiValidationError("createUserDto", "name", value, "NotBlank", "field required"));
        expectedValidationErrors.add(new ApiValidationError("createUserDto", "password", value, "NotBlank", "field required"));
        if (isNotEmpty(value)) {
            expectedValidationErrors.add(new ApiValidationError("createUserDto", "email", value, "Email", "must be an email"));
        }

        var responseJson = mockMvc.perform(post("/api/v1/users")
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", equalTo("Validation error")))
                .andReturn().getResponse().getContentAsString();

        var apiError = objectMapper.readValue(responseJson, ApiError.class);

        assertThat(apiError.getValidationErrors()).containsExactlyInAnyOrderElementsOf(expectedValidationErrors);
    }

    @Test
    void shouldReturnValidationErrorWhenCreatingUserWithEmailIsInvalid() throws Exception {

        var jsonBody = objectMapper.writeValueAsString(new CreateUserDto("abc", "123", "aaa"));

        mockMvc.perform(post("/api/v1/users")
                        .content(jsonBody)
                        .contentType(APPLICATION_JSON))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.message", equalTo("Validation error")),
                        jsonPath("$.validationErrors[0].object", equalTo("createUserDto")),
                        jsonPath("$.validationErrors[0].field", equalTo("email")),
                        jsonPath("$.validationErrors[0].rejectedValue", equalTo("abc")),
                        jsonPath("$.validationErrors[0].code", equalTo("Email")),
                        jsonPath("$.validationErrors[0].message", equalTo("must be an email"))
                );
    }

    @Test
    void shouldForbidUserUpdateWhenAuthenticatedUserIsNotAdmin() throws Exception {

        mockMvc.perform(patch("/api/v1/users/1/subscriptionType/PREMIUM").with(jwt()))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnNoContentIfChangesDoesNotApply() throws Exception {

        when(userService.update(1L, PREMIUM)).thenThrow(new NoChangeRequiredException());

        var jwt = jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"));

        mockMvc.perform(patch("/api/v1/users/1/subscriptionType/PREMIUM").with(jwt))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateUser() throws Exception {

        when(userService.update(1L, PREMIUM)).thenReturn(simpleUserDto);

        var jwt = jwt().authorities(new SimpleGrantedAuthority("SCOPE_ADMIN"));

        mockMvc.perform(patch("/api/v1/users/1/subscriptionType/PREMIUM").with(jwt))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.email", equalTo(simpleUserDto.email())),
                        jsonPath("$.name", equalTo(simpleUserDto.name())),
                        jsonPath("$.isPremium", equalTo(simpleUserDto.isPremium())),
                        jsonPath("$.isAdmin", equalTo(simpleUserDto.isAdmin()))
                );
    }
}
