package com.bix.imageprocessor.web.dto.user;

public record ViewUserDto(
        String email,
        String name,
        String subscriptionType
) {
}
