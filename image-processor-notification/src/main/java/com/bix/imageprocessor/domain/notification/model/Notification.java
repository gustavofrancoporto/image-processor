package com.bix.imageprocessor.domain.notification.model;

public record Notification(
        long imageTransformationsId,
        String imageFileName,
        String email,
        boolean success
) {

}
