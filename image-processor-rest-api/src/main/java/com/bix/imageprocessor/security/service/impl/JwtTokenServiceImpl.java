package com.bix.imageprocessor.security.service.impl;

import com.bix.imageprocessor.domain.user.model.User;
import com.bix.imageprocessor.persistence.repository.UserRepository;
import com.bix.imageprocessor.security.service.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.MINUTES;

@Service
@RequiredArgsConstructor
public class JwtTokenServiceImpl implements JwtTokenService {

    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;

    @Override
    public Jwt createToken(User user) {
        var now = Instant.now();

        var claims = JwtClaimsSet.builder()
                .issuer("image-processor-rest-api")
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plus(15, MINUTES))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .claim("subscriptionType", user.getSubscription().getType())
                .claim("scope", user.getRoleNames())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    @Override
    public User getUser(Jwt jwt) {

        var userId = Long.valueOf(jwt.getSubject());

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
