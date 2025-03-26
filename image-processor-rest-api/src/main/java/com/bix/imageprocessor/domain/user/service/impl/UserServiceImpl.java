package com.bix.imageprocessor.domain.user.service.impl;

import com.bix.imageprocessor.domain.user.model.Role;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.persistence.RoleRepository;
import com.bix.imageprocessor.persistence.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void create(String username, String password) {
        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var userFromDb = userRepository.findByEmail(username);
        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setEmail(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByEmail(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }
}