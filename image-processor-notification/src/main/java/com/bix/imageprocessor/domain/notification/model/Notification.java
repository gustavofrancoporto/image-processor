package com.bix.imageprocessor.domain.notification.model;

public record Notification(
        Long imageTransformationsId,
        Long imageTransformationsStatus,
        String imageFileName,
        String email
) {

}
