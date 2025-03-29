package com.bix.imageprocessor.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "Credentials")
public record AuthRequestDto(
        @NotBlank
        @Email
        @Schema(description = "User e-mail", example = "user@mail.com")
        String email,

        @NotBlank
        @Schema(description = "User password", example = "**********")
        String password) {
}