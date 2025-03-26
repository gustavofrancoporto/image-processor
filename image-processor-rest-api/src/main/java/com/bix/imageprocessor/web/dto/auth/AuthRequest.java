package com.bix.imageprocessor.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(@NotBlank@Email String username, @NotBlank String password) {
}