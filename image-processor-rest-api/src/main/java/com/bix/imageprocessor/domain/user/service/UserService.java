package com.bix.imageprocessor.domain.user.service;

import com.bix.imageprocessor.domain.subscription.model.SubscriptionType;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import com.bix.imageprocessor.web.dto.user.UserDto;

import java.util.Optional;


public interface UserService {

    User create(CreateUserDto createUserDto);

    Optional<UserDto> findById(Long id);

    Optional<User> authenticate(String email, String password);

    UserDto update(Long id, SubscriptionType subscriptionType);
}