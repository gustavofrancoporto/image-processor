package com.bix.imageprocessor.domain.user.service.impl;

import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.persistence.repository.RoleRepository;
import com.bix.imageprocessor.persistence.repository.SubscriptionRepository;
import com.bix.imageprocessor.persistence.repository.UserRepository;
import com.bix.imageprocessor.web.dto.user.UserDto;
import com.bix.imageprocessor.web.exception.model.UserAlreadyExistsException;
import com.bix.imageprocessor.web.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

import static com.bix.imageprocessor.domain.subscription.model.SubscriptionType.BASIC;
import static com.bix.imageprocessor.domain.subscription.model.SubscriptionType.PREMIUM;
import static com.bix.imageprocessor.domain.user.model.Role.ROLE_ADMIN;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User create(UserDto userDto) {

        userRepository.findByEmail(userDto.email()).ifPresent(user -> {
            throw new UserAlreadyExistsException();
        });

        var user = userMapper.convert(userDto);

        var subscriptionType = userDto.isPremium() ? PREMIUM : BASIC;
        var subscription = subscriptionRepository.findByType(subscriptionType);

        user.setSubscription(subscription);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userDto.isAdmin()) {
            var role = roleRepository.findByName(ROLE_ADMIN);
            user.setRoles(Set.of(role));
        }

        return userRepository.save(user);
    }

    @Override
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::convert);
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }
}