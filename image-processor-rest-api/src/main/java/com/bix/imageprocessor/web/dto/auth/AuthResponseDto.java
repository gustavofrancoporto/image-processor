package com.bix.imageprocessor.web.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Authentication Response")
public record AuthResponseDto(
        @Schema(description = "Bearer token")
        String accessToken
) {
}