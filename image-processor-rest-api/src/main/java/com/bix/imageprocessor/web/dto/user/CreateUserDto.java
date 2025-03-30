package com.bix.imageprocessor.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(name = "User")
public record CreateUserDto(
        @NotBlank
        @Email
        @Schema(description = "User e-mail.")
        String email,

        @NotBlank
        @Schema(description = "User password.")
        String password,

        @NotBlank
        @Schema(description = "User name.")
        String name
) {
}