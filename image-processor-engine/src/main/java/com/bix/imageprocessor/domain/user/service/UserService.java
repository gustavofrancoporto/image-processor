package com.bix.imageprocessor.domain.user.service;

import com.bix.imageprocessor.domain.user.model.User;

import java.util.Optional;


public interface UserService {

    void create(String username, String password);

    Optional<User> authenticate(String username, String password);

}