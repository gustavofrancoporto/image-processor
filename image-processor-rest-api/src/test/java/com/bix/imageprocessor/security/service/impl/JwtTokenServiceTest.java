package com.bix.imageprocessor.security.service.impl;

import com.bix.imageprocessor.config.SecurityConfiguration;
import com.bix.imageprocessor.domain.subscription.model.Subscription;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.persistence.repository.UserRepository;
import com.bix.imageprocessor.security.service.JwtTokenService;
import com.bix.imageprocessor.web.controller.AuthenticationController;
import com.bix.imageprocessor.web.exception.service.impl.DefaultApiValidationErrorFactory;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static com.bix.imageprocessor.domain.subscription.model.SubscriptionType.PREMIUM;
import static java.time.Instant.now;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebMvcTest(AuthenticationController.class)
@Import({SecurityConfiguration.class, DefaultApiValidationErrorFactory.class, JwtTokenServiceImpl.class})
@ActiveProfiles("test")
public class JwtTokenServiceTest {

    @Autowired JwtTokenService jwtTokenService;

    @MockitoBean UserService userService;
    @MockitoBean UserRepository userRepository;

    @Mock User user;
    @Mock Subscription subscription;
    @Mock Jwt jwt;

    Long userId = 1L;

    @Test
    void shouldCreateToken() {

        var userName = "Joe Doe";
        var userEmail = "joedoe@mail.com";
        var userSubscriptionType = PREMIUM;
        var userRoleNames = "ADMIN";

        when(user.getId()).thenReturn(userId);
        when(user.getName()).thenReturn(userName);
        when(user.getEmail()).thenReturn(userEmail);
        when(user.getSubscription()).thenReturn(subscription);
        when(subscription.getType()).thenReturn(userSubscriptionType);
        when(user.getRoleNames()).thenReturn(userRoleNames);

        var jwt = jwtTokenService.createToken(user);

        assertThat(jwt.getSubject()).isEqualTo(String.valueOf(userId));
        assertThat(jwt.getIssuedAt()).isCloseTo(now(), within(15, SECONDS));
        assertThat(jwt.getExpiresAt()).isCloseTo(now().plus(15, MINUTES), within(15, SECONDS));
        assertThat(jwt.getClaims()).containsEntry("name", userName);
        assertThat(jwt.getClaims()).containsEntry("email", userEmail);
        assertThat(jwt.getClaims()).containsEntry("subscriptionType", userSubscriptionType);
        assertThat(jwt.getClaims()).containsEntry("scope", userRoleNames);
    }

    @Test
    void shouldGetUserByJwt() {

        when(jwt.getSubject()).thenReturn(userId.toString());
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        var userObtained = jwtTokenService.getUser(jwt);

        assertThat(userObtained).isEqualTo(user);
    }

    @Test
    void shouldThrowExceptionWhenGettingNonExistentUserByJwt() {

        when(jwt.getSubject()).thenReturn(userId.toString());
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> jwtTokenService.getUser(jwt))
                .withMessage("User not found");
    }
}
