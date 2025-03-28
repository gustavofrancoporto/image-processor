package com.bix.imageprocessor.domain.image.messaging;

import com.bix.imageprocessor.domain.image.service.ImageTransformService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static com.bix.imageprocessor.config.RabbitMqConfig.IMAGE_PROCESSOR_QUEUE_NAME;

@Component
@RequiredArgsConstructor
public class ImageTransformQueueConsumer {

    private final ImageTransformService imageTransformService;

    @RabbitListener(queues = {IMAGE_PROCESSOR_QUEUE_NAME})
    public void receive(@Payload String imageTransformIdText) {
        var imageTransformId = Long.valueOf(imageTransformIdText);
        imageTransformService.process(imageTransformId);
    }
}
