package com.bix.imageprocessor.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequestDto(@NotBlank@Email String email, @NotBlank String password) {
}