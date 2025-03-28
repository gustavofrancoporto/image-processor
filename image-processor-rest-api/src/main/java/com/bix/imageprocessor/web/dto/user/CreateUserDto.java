package com.bix.imageprocessor.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserDto(
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String name,
        @NotNull Boolean isPremium
) {
}