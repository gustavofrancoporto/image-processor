package com.bix.imageprocessor.domain.notification.service.impl;

import com.bix.imageprocessor.domain.notification.model.Notification;
import com.bix.imageprocessor.domain.notification.service.EmailContentGenerator;
import com.bix.imageprocessor.domain.notification.service.NotificationService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

import static jakarta.mail.Message.RecipientType.TO;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final List<? extends EmailContentGenerator> emailContentGenerators;
    private final JavaMailSender mailSender;

    @Override
    public void notify(Notification notification) {

        try {
            var generator = findGenerator(notification);
            var message = createMessage(notification, generator);
            mailSender.send(message);
            log.info("Notification {} sent successfully", notification);
        } catch (Exception e) {
            log.error("Error sending notification {}", notification, e);
        }
    }

    private EmailContentGenerator findGenerator(Notification notification) {
        return emailContentGenerators.stream()
                .filter(g -> g.apply(notification))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No HtmlMessageGenerator found for notification " + notification));
    }

    private MimeMessage createMessage(Notification notification, EmailContentGenerator generator) throws MessagingException {
        var subject = generator.generateSubject(notification);
        var htmlContent = generator.generateMessage(notification);

        var message = mailSender.createMimeMessage();
        message.setRecipients(TO, notification.email());
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=utf-8");
        return message;
    }
}
