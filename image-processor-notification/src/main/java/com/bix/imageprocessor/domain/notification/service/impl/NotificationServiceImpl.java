package com.bix.imageprocessor.domain.notification.service.impl;

import com.bix.imageprocessor.domain.notification.service.NotificationService;
import com.bix.imageprocessor.persistence.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final JavaMailSender mailSender;

    @Override
    public void notify(Long imageTransformId) {

        var notification = notificationRepository.findByImageTransformId(imageTransformId);

        var message = new SimpleMailMessage();
        message.setFrom("noreply@imageprocessor.com");
        message.setTo(notification.email());
        message.setSubject("Image Transform Completed");
        message.setText(String.format("The transformation of the image %s was completed.", notification.imageFileName()));
        mailSender.send(message);
    }
}
