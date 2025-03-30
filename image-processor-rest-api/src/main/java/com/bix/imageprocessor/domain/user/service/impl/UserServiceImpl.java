package com.bix.imageprocessor.domain.user.service.impl;

import com.bix.imageprocessor.domain.subscription.model.SubscriptionType;
import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.persistence.repository.RoleRepository;
import com.bix.imageprocessor.persistence.repository.SubscriptionRepository;
import com.bix.imageprocessor.persistence.repository.UserRepository;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import com.bix.imageprocessor.web.dto.user.UserDto;
import com.bix.imageprocessor.web.exception.model.NoChangeRequiredException;
import com.bix.imageprocessor.web.exception.model.UserAlreadyExistsException;
import com.bix.imageprocessor.web.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bix.imageprocessor.domain.subscription.model.SubscriptionType.BASIC;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public User create(CreateUserDto createUserDto) {

        userRepository.findByEmail(createUserDto.email()).ifPresent(user -> {
            throw new UserAlreadyExistsException();
        });

        var user = userMapper.convert(createUserDto);

        var subscription = subscriptionRepository.findByType(BASIC);
        user.setSubscription(subscription);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

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

    @Override
    public UserDto update(Long id, SubscriptionType subscriptionType) {

        var user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (user.getSubscription().getType() == subscriptionType) {
            throw new NoChangeRequiredException();
        }

        var subscription = subscriptionRepository.findByType(subscriptionType);
        user.setSubscription(subscription);
        user = userRepository.save(user);

        return userMapper.convert(user);
    }
}