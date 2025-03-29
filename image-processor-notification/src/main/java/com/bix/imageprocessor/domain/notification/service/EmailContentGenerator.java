package com.bix.imageprocessor.domain.notification.service;

import com.bix.imageprocessor.domain.notification.model.Notification;

public interface EmailContentGenerator {

    String generateSubject(Notification notification);

    String generateMessage(Notification notification);

    boolean apply(Notification notification);
}
