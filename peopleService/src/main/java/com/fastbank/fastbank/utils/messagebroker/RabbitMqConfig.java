package com.fastbank.fastbank.utils.messagebroker;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMq configuration for the FastBank People Service.
 *
 * <p>Declares the queue, topic exchange, binding, JSON message converter, and RabbitTemplate beans
 * used for publishing person creation events.
 */
@Configuration
public class RabbitMqConfig {

  /** Name of the durable queue that receives person events. */
  public static final String QUEUE_NAME = "peopleQueue";

  /** Name of the topic exchange to which person events are published. */
  public static final String EXCHANGE_NAME = "people-event";

  /** Routing key used when publishing person creation events. */
  public static final String ROUTING_KEY = "people.created";

  /**
   * Declares a durable queue named {@value #QUEUE_NAME}.
   *
   * @return the configured {@link Queue}
   */
  @Bean
  public Queue peopleQueue() {
    return new Queue(QUEUE_NAME, true);
  }

  /**
   * Declares a topic exchange named {@value #EXCHANGE_NAME}.
   *
   * @return the configured {@link TopicExchange}
   */
  @Bean
  public TopicExchange exchange() {
    return new TopicExchange(EXCHANGE_NAME);
  }

  /**
   * Binds the people queue to the exchange using the routing key {@value #ROUTING_KEY}.
   *
   * @param queue the queue to bind
   * @param exchange the exchange to bind to
   * @return the configured {@link Binding}
   */
  @Bean
  public Binding binding(Queue queue, TopicExchange exchange) {
    return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
  }

  /**
   * Creates a JSON message converter for serialising and deserialising AMQP messages.
   *
   * @return the configured {@link Jackson2JsonMessageConverter}
   */
  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  /**
   * Creates a {@link RabbitTemplate} configured with the JSON message converter.
   *
   * @param connectionFactory the AMQP connection factory
   * @return the configured {@link RabbitTemplate}
   */
  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter());
    return template;
  }
}
