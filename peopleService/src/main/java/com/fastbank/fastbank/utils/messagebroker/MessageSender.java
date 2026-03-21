package com.fastbank.fastbank.utils.messagebroker;

import com.fastbank.fastbank.model.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service responsible for publishing messages to RabbitMQ.
 *
 * <p>Sends {@link Person} objects to the configured exchange using the defined routing key.
 */
@Service
@Slf4j
public class MessageSender {

  @Autowired private RabbitTemplate rabbitTemplate;

  /**
   * Publishes a {@link Person} message to the RabbitMQ exchange.
   *
   * @param person the person entity to send as a message payload
   */
  public void sendMessage(Person person) {
    rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, person);
    log.info("Sent: {}", person);
  }
}
