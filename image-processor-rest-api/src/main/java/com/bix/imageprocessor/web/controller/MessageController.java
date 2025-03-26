package com.bix.imageprocessor.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bix.imageprocessor.config.RabbitMqConfig.IMAGE_PROCESSOR_QUEUE_NAME;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping
    public void send() {
        rabbitTemplate.convertAndSend(IMAGE_PROCESSOR_QUEUE_NAME, "Hello from RabbitMQ!");
    }
}