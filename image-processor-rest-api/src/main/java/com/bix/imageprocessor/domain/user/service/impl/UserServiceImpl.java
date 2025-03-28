package com.bix.imageprocessor.domain.user.service.impl;

import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.persistence.repository.SubscriptionRepository;
import com.bix.imageprocessor.persistence.repository.UserRepository;
import com.bix.imageprocessor.web.dto.user.CreateUserDto;
import com.bix.imageprocessor.web.dto.user.ViewUserDto;
import com.bix.imageprocessor.web.exception.model.UserAlreadyExistsException;
import com.bix.imageprocessor.web.mapper.CreateUserMapper;
import com.bix.imageprocessor.web.mapper.ViewUserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.bix.imageprocessor.domain.subscription.model.SubscriptionType.BASIC;
import static com.bix.imageprocessor.domain.subscription.model.SubscriptionType.PREMIUM;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PasswordEncoder passwordEncoder;
    private final ViewUserMapper viewUserMapper;
    private final CreateUserMapper createUserMapper;

    @Override
    @Transactional
    public User create(CreateUserDto userDto) {

        userRepository.findByEmail(userDto.email()).ifPresent(user -> {
            throw new UserAlreadyExistsException();
        });

        var user = createUserMapper.convert(userDto);

        var subscriptionType = userDto.isPremium() ? PREMIUM : BASIC;
        var subscription = subscriptionRepository.findByType(subscriptionType);

        user.setSubscription(subscription);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    @Override
    public Optional<ViewUserDto> findById(Long id) {
        return userRepository.findById(id)
                .map(viewUserMapper::convert);
    }

    @Override
    public Optional<User> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }
}