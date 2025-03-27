package com.bix.imageprocessor.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
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
    Queue imageResizingQueue() {
        return new Queue(IMAGE_PROCESSOR_QUEUE_NAME);
    }
//
//    @Bean
//    Binding imageResizingBinding(TopicExchange exchange) {
//        var queue = imageResizingQueue();
//        return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
//    }
}
