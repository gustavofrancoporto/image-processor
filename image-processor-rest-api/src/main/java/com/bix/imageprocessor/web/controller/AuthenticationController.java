package com.bix.imageprocessor.web.controller;

import com.bix.imageprocessor.domain.user.service.UserService;
import com.bix.imageprocessor.security.service.JwtTokenService;
import com.bix.imageprocessor.web.dto.auth.AuthRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @PostMapping("/login")
    @Operation(summary = "Get access token given the credentials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content)
    })
    public String authenticate(@RequestBody @Validated AuthRequestDto authRequestDto) {

        var user = userService.authenticate(authRequestDto.email(), authRequestDto.password())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        var token = jwtTokenService.createToken(user);

        return token.getTokenValue();
    }
}