package com.bix.imageprocessor.domain.user.service.impl;

import com.bix.imageprocessor.domain.subscription.model.Subscription;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.persistence.repository.SubscriptionRepository;
import com.bix.imageprocessor.persistence.repository.UserRepository;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import com.bix.imageprocessor.web.dto.user.UserDto;
import com.bix.imageprocessor.web.exception.model.NoChangeRequiredException;
import com.bix.imageprocessor.web.exception.model.UserAlreadyExistsException;
import com.bix.imageprocessor.web.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.bix.imageprocessor.domain.subscription.model.SubscriptionType.BASIC;
import static com.bix.imageprocessor.domain.subscription.model.SubscriptionType.PREMIUM;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock SubscriptionRepository subscriptionRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock UserMapper userMapper;

    @InjectMocks UserServiceImpl userService;

    @Mock User user;
    @Mock UserDto userDto;
    @Mock CreateUserDto createUserDto;
    @Mock Subscription subscription;

    Long userId = 1L;
    String userEmail = "user@email.com";
    String userPassword = "123";
    String userPasswordEncoded = "***";

    @Test
    void shouldFailWhenCreatingUserHavingEmailThatAlreadyExists() {

        when(createUserDto.email()).thenReturn(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));

        assertThatExceptionOfType(UserAlreadyExistsException.class)
                .isThrownBy(() -> userService.create(createUserDto));
    }

    @Test
    void shouldCreateNewUser() {

        when(createUserDto.email()).thenReturn(userEmail);
        when(user.getPassword()).thenReturn(userPassword);
        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());
        when(subscriptionRepository.findByType(BASIC)).thenReturn(subscription);
        when(userMapper.convert(createUserDto)).thenReturn(user);
        when(passwordEncoder.encode(userPassword)).thenReturn(userPasswordEncoded);
        when(userRepository.save(user)).thenReturn(user);

        var savedUser = userService.create(createUserDto);

        assertThat(savedUser).isEqualTo(user);

        verify(user).setSubscription(subscription);
        verify(user).setPassword(userPasswordEncoded);
    }

    @Test
    void shouldFindUserById() {

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.convert(user)).thenReturn(userDto);

        var result = userService.findById(userId);

        assertThat(result).contains(userDto);
    }

    @Test
    void shouldReturnUserWhenCredentialsAreValid() {

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(userPasswordEncoded);
        when(passwordEncoder.matches(userPasswordEncoded, userPasswordEncoded)).thenReturn(true);

        var result = userService.authenticate(userEmail, userPasswordEncoded);

        assertThat(result).contains(user);
    }

    @Test
    void shouldReturnEmptyWhenCredentialsAreInvalid() {

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(user.getPassword()).thenReturn(userPasswordEncoded);
        when(passwordEncoder.matches(userPasswordEncoded, userPasswordEncoded)).thenReturn(false);

        var result = userService.authenticate(userEmail, userPasswordEncoded);

        assertThat(result).isEmpty();
    }

    @Test
    void shouldFailWhenChangingSubscriptionOfUserThatDoesNotExist() {

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> userService.changeSubscription(userId, BASIC))
                .withMessage("User not found");
    }

    @Test
    void shouldFailWhenUserHasAlreadyTheSubscriptionPassedAsParameter() {

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getSubscription()).thenReturn(subscription);
        when(subscription.getType()).thenReturn(BASIC);

        assertThatExceptionOfType(NoChangeRequiredException.class)
                .isThrownBy(() -> userService.changeSubscription(userId, BASIC));
    }

    @Test
    void shouldChangeSubscriptionOfUser() {

        var newSubscriptionType = PREMIUM;
        var newSubscription = mock(Subscription.class);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.getSubscription()).thenReturn(subscription);
        when(subscription.getType()).thenReturn(BASIC);
        when(subscriptionRepository.findByType(newSubscriptionType)).thenReturn(newSubscription);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.convert(user)).thenReturn(userDto);

        var updatedUser = userService.changeSubscription(userId, newSubscriptionType);

        verify(user).setSubscription(newSubscription);

        assertThat(updatedUser).isEqualTo(userDto);
    }
}
