package com.bix.imageprocessor.domain.user.service;

import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.web.dto.user.UserDto;

import java.util.Optional;


public interface UserService {

    User create(UserDto userDto);

    Optional<UserDto> findById(Long id);

    Optional<User> authenticate(String username, String password);

}