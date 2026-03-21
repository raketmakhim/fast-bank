package com.fastbank.accountservice.messagebroker;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for the FastBank Account Service.
 *
 * <p>Declares the durable queue used to consume person creation events published by the
 * People Service.
 */
@Configuration
public class RabbitMQConfig {

  /** Name of the durable queue from which person creation events are consumed. */
  public static final String QUEUE_NAME = "peopleQueue";

  /**
   * Declares a durable queue named {@value #QUEUE_NAME}.
   *
   * @return the configured {@link Queue}
   */
  @Bean
  public Queue peopleQueue() {
    return new Queue(QUEUE_NAME, true);
  }
}
