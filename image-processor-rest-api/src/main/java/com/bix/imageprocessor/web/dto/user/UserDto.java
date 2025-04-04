package com.bix.imageprocessor.web.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
@Schema(name = "User")
public record UserDto(
        @NotBlank
        @Email
        @Schema(description = "User e-mail.")
        String email,

        @NotBlank
        @Schema(description = "User name.")
        String name,

        @NotNull
        @Schema(description = "Whether the user's subscription is premium.")
        Boolean isPremium,

        @NotNull
        @Schema(description = "Whether it's an admin user.")
        Boolean isAdmin
) {
}