package com.bix.imageprocessor.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private final static String TOPIC_EXCHANGE_NAME = "imageProcessorTopicExchange";
    public final static String IMAGE_PROCESSOR_QUEUE_NAME = "imageProcessorQueue";

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Queue queue() {
        return new Queue(IMAGE_PROCESSOR_QUEUE_NAME);
    }
}
