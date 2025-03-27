package com.bix.imageprocessor.domain.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static com.bix.imageprocessor.config.RabbitMqConfig.IMAGE_PROCESSOR_QUEUE_NAME;

@Service
@RequiredArgsConstructor
public class ImageTransformQueueConsumer {

    private final ImageTransformService imageProcessor;

    @RabbitListener(queues = {IMAGE_PROCESSOR_QUEUE_NAME})
    public void receive(@Payload String imageTransformIdText) {
        var imageTransformId = Long.valueOf(imageTransformIdText);
        imageProcessor.process(imageTransformId);
    }
}
