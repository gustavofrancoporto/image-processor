package com.bix.imageprocessor.security.service;

import com.bix.imageprocessor.domain.user.model.User;
import org.springframework.security.oauth2.jwt.Jwt;

public interface JwtTokenService {
    Jwt createToken(User user);
}
