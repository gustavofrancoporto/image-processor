package com.bix.imageprocessor.domain.image.service.impl;

import com.bix.imageprocessor.domain.image.service.ImageTransformQueueSender;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ImageTransformQueueSenderImpl implements ImageTransformQueueSender {

    private final RabbitTemplate rabbitTemplate;
    private final Queue queue;

    @Override
    public void send(Long imageTransformId) {
        rabbitTemplate.convertAndSend(queue.getName(), imageTransformId);
    }

}
