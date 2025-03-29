package com.bix.imageprocessor.domain.notification.model;

import lombok.Builder;

@Builder
public record NotificationMessage(
        long imageTransformationId,
        String imageFileName,
        String email,
        boolean success
) {
}
