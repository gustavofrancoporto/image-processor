package com.bix.imageprocessor.domain.notification.model;

import lombok.Builder;

@Builder
public record NotificationMessage(
        String imageCode,
        String imageFileName,
        String email,
        boolean success
) {
}
