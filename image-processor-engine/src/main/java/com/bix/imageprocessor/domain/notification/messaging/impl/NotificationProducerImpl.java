package com.bix.imageprocessor.domain.notification.messaging.impl;

import com.bix.imageprocessor.domain.image.model.ImageTransform;
import com.bix.imageprocessor.domain.notification.messaging.NotificationProducer;
import com.bix.imageprocessor.domain.notification.model.NotificationMessage;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.bix.imageprocessor.config.RabbitMqConfig.NOTIFICATION_QUEUE_NAME;

@Service
public class NotificationProducerImpl implements NotificationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    public NotificationProducerImpl(RabbitTemplate rabbitTemplate, @Qualifier(NOTIFICATION_QUEUE_NAME) Queue queue) {
        this.rabbitTemplate = rabbitTemplate;
        this.queue = queue;
    }

    @Override
    public void notify(ImageTransform imageTransform, boolean success) {

        var notificationMessage = NotificationMessage.builder()
                .imageTransformationId(imageTransform.id())
                .imageFileName(imageTransform.imageFileName())
                .email(imageTransform.requestorEmail())
                .success(success)
                .build();

        rabbitTemplate.convertAndSend(queue.getName(), notificationMessage);
    }
}
