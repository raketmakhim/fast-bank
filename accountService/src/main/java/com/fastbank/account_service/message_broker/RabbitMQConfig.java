package com.fastbank.account_service.message_broker;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "peopleQueue";

    @Bean
    public Queue peopleQueue() {
        return new Queue(QUEUE_NAME, true);
    }
}
