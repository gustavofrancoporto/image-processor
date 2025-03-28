package com.bix.imageprocessor.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    private final static String TOPIC_EXCHANGE_NAME = "imageProcessorTopicExchange";
    public final static String IMAGE_PROCESSOR_QUEUE_NAME = "imageProcessorQueue";
    public final static String NOTIFICATION_QUEUE_NAME = "notificationQueue";

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE_NAME);
    }

    @Bean
    Queue imageProcessorQueue() {
        return new Queue(IMAGE_PROCESSOR_QUEUE_NAME);
    }

    @Bean
    @Qualifier(NOTIFICATION_QUEUE_NAME)
    Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE_NAME);
    }
}
