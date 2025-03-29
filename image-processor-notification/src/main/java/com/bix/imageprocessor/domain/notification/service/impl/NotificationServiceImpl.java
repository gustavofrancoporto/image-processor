package com.bix.imageprocessor.domain.notification.service.impl;

import com.bix.imageprocessor.domain.notification.model.Notification;
import com.bix.imageprocessor.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;

    @Override
    public void notify(Notification notification) {

        log.info("Sending notification for image transform {}", notification);

        try {
            var subject = notification.success() ? "Image Transformation Completed" : "Image Transformation Failed";
            var message = getMailMessage(notification, subject);

            mailSender.send(message);
            log.info("Notification sent successfully");
        } catch (Exception e) {
            log.error("Error sending notification", e);
        }
    }

    private static SimpleMailMessage getMailMessage(Notification notification, String subject) {
        var text = notification.success() ?
                "The transformation of the image " + notification.imageFileName() + " was completed." :
                "The transformation of the image " + notification.imageFileName() + " failed. Please, try it again.";

        var message = new SimpleMailMessage();
        message.setFrom("noreply@imageprocessor.com");
        message.setTo(notification.email());
        message.setSubject(subject);
        message.setText(text);
        return message;
    }
}
