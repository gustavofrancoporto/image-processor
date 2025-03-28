package com.bix.imageprocessor.domain.user.service;

import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import com.bix.imageprocessor.web.dto.user.ViewUserDto;

import java.util.Optional;


public interface UserService {

    User create(CreateUserDto userDto);

    Optional<ViewUserDto> findById(Long id);

    Optional<User> authenticate(String username, String password);

}