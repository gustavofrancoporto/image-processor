package com.bix.imageprocessor.domain.notification.messaging;

import com.bix.imageprocessor.domain.notification.model.Notification;
import com.bix.imageprocessor.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.bix.imageprocessor.config.RabbitMqConfig.NOTIFICATION_QUEUE_NAME;
@Component
@RequiredArgsConstructor
public class NotificationQueueConsumer {

    private final NotificationService notificationService;

    @RabbitListener(queues = {NOTIFICATION_QUEUE_NAME})
    public void receive(@Payload Notification notification) {
        notificationService.notify(notification);
    }
}
