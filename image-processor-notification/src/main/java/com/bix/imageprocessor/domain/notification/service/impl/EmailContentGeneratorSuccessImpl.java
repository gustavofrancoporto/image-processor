package com.bix.imageprocessor.domain.notification.service.impl;

import com.bix.imageprocessor.domain.notification.model.Notification;
import com.bix.imageprocessor.domain.notification.service.EmailContentGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailContentGeneratorSuccessImpl implements EmailContentGenerator {

    private final String baseUrl;
    private final TemplateEngine templateEngine;

    public EmailContentGeneratorSuccessImpl(@Value("${app.image-download-base-url}") String baseUrl, TemplateEngine templateEngine) {
        this.baseUrl = baseUrl;
        this.templateEngine = templateEngine;
    }

    @Override
    public String generateSubject(Notification notification) {
        return "Image Transformation Completed";
    }

    @Override
    public String generateMessage(Notification notification) {

        var downloadUrl = createDownloadUrl(notification.imageCode());

        var context = new Context();
        context.setVariable("imageFileName", notification.imageFileName());
        context.setVariable("downloadUrl", downloadUrl);

        return templateEngine.process("success-notification", context);
    }

    private String createDownloadUrl(String imageCode) {
        return baseUrl + "api/v1/images/" + imageCode;
    }

    @Override
    public boolean apply(Notification notification) {
        return notification.success();
    }
}