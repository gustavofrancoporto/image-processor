package com.bix.imageprocessor.domain.notification.messaging;

import com.bix.imageprocessor.domain.image.model.ImageTransform;

public interface NotificationProducer {
    void notify(ImageTransform imageTransform, boolean success);
}
