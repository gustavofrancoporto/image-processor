package com.bix.imageprocessor.domain.notification.messaging;

public interface NotificationProducer {
    void notify(Long imageTransformId);
}
