package com.fastbank.account_service.message_broker;

import com.fastbank.account_service.model.dto.PersonRecord;
import com.fastbank.account_service.email.EmailService;
import com.fastbank.account_service.service.AccountRegistrationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private final AccountRegistrationService accountRegistrationService;

    public MessageListener(AccountRegistrationService accountRegistrationService) {
        this.accountRegistrationService = accountRegistrationService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message) throws JsonProcessingException {
        System.out.println("Received: " + message);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            PersonRecord person = objectMapper.readValue(message, PersonRecord.class);

            accountRegistrationService.registerAccount(person);
        }
        catch (Exception e) {
            System.out.println("Failed to process message: " + message);
        }// Process your message here
    }
}
