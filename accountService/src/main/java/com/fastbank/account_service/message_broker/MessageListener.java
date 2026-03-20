package com.fastbank.account_service.message_broker;

import com.fastbank.account_service.model.dto.PersonRecord;
import com.fastbank.account_service.service.AccountRegistrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MessageListener {

    private final AccountRegistrationService accountRegistrationService;

    public MessageListener(AccountRegistrationService accountRegistrationService) {
        this.accountRegistrationService = accountRegistrationService;
    }

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
