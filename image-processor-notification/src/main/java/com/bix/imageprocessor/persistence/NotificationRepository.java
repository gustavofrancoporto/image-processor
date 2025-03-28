package com.bix.imageprocessor.persistence;

import com.bix.imageprocessor.domain.notification.model.Notification;

public interface NotificationRepository {
    Notification findByImageTransformId(Long id);
}
