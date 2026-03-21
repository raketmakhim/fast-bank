package com.fastbank.accountservice.messagebroker;

import com.fastbank.accountservice.model.dto.PersonRecord;
import com.fastbank.accountservice.service.AccountRegistrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ message listener for the FastBank Account Service.
 *
 * <p>Consumes person creation events from the {@code peopleQueue} and delegates account
 * registration to {@link AccountRegistrationService}.
 */
@Component
@Slf4j
public class MessageListener {

  private final AccountRegistrationService accountRegistrationService;

  /**
   * Constructs a {@code MessageListener} with the given {@link AccountRegistrationService}.
   *
   * @param accountRegistrationService the service used to register a new account for a person
   */
  public MessageListener(AccountRegistrationService accountRegistrationService) {
    this.accountRegistrationService = accountRegistrationService;
  }

  /**
   * Handles an incoming RabbitMQ message from the people queue.
   *
   * <p>Deserialises the JSON payload into a {@link PersonRecord} and triggers account registration.
   * Logs an error if deserialisation fails.
   *
   * @param message the raw JSON string received from the queue
   */
  @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
  public void receiveMessage(String message) {
    log.info("Received: {}", message);

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      PersonRecord person = objectMapper.readValue(message, PersonRecord.class);
      accountRegistrationService.registerAccount(person);
    } catch (JsonProcessingException e) {
      log.error("Failed to process message: {}", message);
    }
  }
}
