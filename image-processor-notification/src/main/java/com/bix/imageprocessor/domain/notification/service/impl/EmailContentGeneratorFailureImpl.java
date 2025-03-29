package com.bix.imageprocessor.domain.notification.service.impl;

import com.bix.imageprocessor.domain.notification.model.Notification;
import com.bix.imageprocessor.domain.notification.service.EmailContentGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailContentGeneratorFailureImpl implements EmailContentGenerator {

    private final TemplateEngine templateEngine;

    @Override
    public String generateSubject(Notification notification) {
        return "Image Transformation Failed";
    }

    @Override
    public String generateMessage(Notification notification) {

        var context = new Context();
        context.setVariable("imageFileName", notification.imageFileName());

        return templateEngine.process("failure-notification", context);
    }

    @Override
    public boolean apply(Notification notification) {
        return !notification.success();
    }
}