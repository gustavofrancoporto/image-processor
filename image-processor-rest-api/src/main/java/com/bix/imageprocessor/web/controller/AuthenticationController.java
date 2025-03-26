package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.security.service.JwtTokenService;
import com.bix.imageprocessor.web.dto.auth.AuthRequestDto;
import com.bix.imageprocessor.web.dto.auth.AuthResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/auth")
    public AuthResponseDto authenticate(@RequestBody @Valid AuthRequestDto loginRequest) {

        var user = userService.authenticate(loginRequest.email(), loginRequest.password())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        var token = jwtTokenService.createToken(user);

        return new AuthResponseDto(token.getTokenValue());
    }
}